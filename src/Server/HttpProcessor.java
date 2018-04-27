package Server;


import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class HttpProcessor implements Runnable{
    Socket socket;
    HttpRequest request;
    HttpResponse response;
    FilterChain filterChain;
    public HttpProcessor(Socket socket) {
        this.socket=socket;
    }

    @Override
    public void run() {
        try {

            System.out.println(socket.getRemoteSocketAddress());
            request = new HttpRequest(socket.getInputStream());
            response = new HttpResponse(socket.getOutputStream());
            //parse request info
            parseRequestLine();
            parseHeaders();
            HttpMapper mapper=HttpMapper.getInstance();
            HttpHandler handler=HttpMapper.getInstance().getHandler(request.getPath());
            filterChain=new FilterChain(handler);
            filterChain.appendAll(mapper.getFilter(request.getPath()));
            filterChain.doFilter(request,response);
            //make response
            response.finishResponse();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
         try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void parseParams(String paramString) {
        String[] ss=paramString.split("&");
        for(String s:ss){
            int equal=s.indexOf("=");
            if(equal==-1)
                continue;
            request.setParam(s.substring(0,equal),s.substring(equal+1));
        }
        }

        public void parseRequestLine() throws IOException {
            InputStream in = request.getInputStream();
            byte[] buff = new byte[4096];
            int pos = 0;
            int b;
            //read first line
            pos+=readLine(in,buff,pos);
            String requestLine = new String(buff, 0, pos);
            String[] ss = requestLine.split(" ");
            String method = ss[0];
            String path = ss[1];
            String version = ss[2];
            request.setMethod(method);
            request.setVersion(version);
            //parse path
            int question = path.indexOf("?");
        if (question != -1) {
            String queryString = path.substring(question);
            parseParams(queryString);
            path = path.substring(0, question);
        }
        request.setPath(path);
    }

    public void parseHeaders() throws IOException {
        InputStream in = request.getInputStream();
        byte[] buff = new byte[8192];
        int b;
        int pos=0;
        //read a line
        while (true){
            pos = 0;
            pos+=readLine(in,buff,pos);
            if(pos==0)
                break;
            //parse a line to a header
            String line = new String(buff, 0, pos);
            int colon = line.indexOf(":");

            String name = line.substring(0, colon);
            String value = line.substring(colon + 2);//start at colon+2 to skip a space
            request.setHeader(name, value);
            if("Cookie".equalsIgnoreCase(name)){
                request.addCookies(parseCookie(name));
            }
        }
    }

    private int readLine(InputStream in, byte[] buff, int offset) throws IOException {
        int pos=offset;
        int b;
        while (true) {
            b = in.read();
            if ('\r' == b) {
                in.read();
                break;
            }
            buff[pos++] = (byte) b;
        }
        return pos-offset;
    }
    private List<Cookie> parseCookie(String cookieString) {
        List<Cookie> cookies = new ArrayList<>();
        while (cookieString.length() > 0) {
            int semicolon = cookieString.indexOf(';');
            if (semicolon < 0)
                semicolon = cookieString.length();
            if (semicolon == 0)
                break;
            String token = cookieString.substring(0, semicolon);
            if (semicolon < cookieString.length())
                cookieString = cookieString.substring(semicolon + 1);
            else
                cookieString = "";
            int equals = token.indexOf('=');
            if (equals > 0) {
                String name = token.substring(0, equals).trim();
                String value = token.substring(equals + 1).trim();
                cookies.add(new Cookie(name, value));
            }
        }
        return cookies;
    }


}
