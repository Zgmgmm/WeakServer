package Server;

import Handler.DefaultErrorHandler;
import org.apache.tika.Tika;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HttpResponse {
    protected static int DEFAULT_BUFFER_SIZE = 8192;
    protected OutputStream out;//real network output stream
    protected boolean committed;
    protected ByteArrayOutputStream buff; // buffer the response body
    protected PrintWriter printWriter;
    protected boolean usingWriter;
    protected Map<String, String> headers = new HashMap<>();
    private int code;
    private String version = "HTTP/1.1";
    private HttpRequest request;

    public HttpResponse(OutputStream out) {
        this.out = out;
        committed = false;
        usingWriter = false;
        code=200;
    }


    public void setRequest(HttpRequest request) {
        this.request = request;
    }


    public void setCode(int code) {
        this.code = code;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public PrintWriter getWriter(){
        if (printWriter != null)
            return printWriter;
        if (buff != null && !usingWriter)
            throw new RuntimeException("using stream!");
        buff = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
        printWriter = new PrintWriter(buff);
        usingWriter = true;
        return printWriter;
    }

    public OutputStream getOutputStream(){
        if (usingWriter)
            throw new RuntimeException("using writer!");
        if (buff == null) {
            buff = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
        }
        return buff;
    }

    public void finishResponse() {
        if(committed)
            return;
        try {
            if(usingWriter)
                printWriter.flush();
            if(buff==null)
                return;
            sendStatusCode();
            sendHeaders();
            sendBody();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                committed=true;
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendStatusCode() throws IOException {
        //send status code
        StringBuilder sb = new StringBuilder("HTTP/1.1 ");
        sb.append(code);
        if (200 == code) {
            sb.append(" OK");
        } else if (404 == code) {
            sb.append(" Not Found");
        };
        sb.append("\r\n");
        out.write(sb.toString().getBytes("utf-8"));
    }

    private void sendBody() throws IOException {
        buff.writeTo(out);
    }


    protected void sendHeaders() throws IOException {
        //set content length
        if(!headers.containsKey("Transfer-Encoding")&&!headers.containsKey("Content-Encoding")) {
            if(buff!=null)
                headers.put("Content-Length", buff.size() + "");
        }
        //set content type if hasn't been set
        if(!headers.containsKey("Content-Type"))
            headers.put("Content-Type","text/html; charset=utf-8");

        for (Map.Entry<String, String> header : headers.entrySet()) {
            out.write((header.getKey() + ": " + header.getValue() + "\r\n").getBytes("utf-8"));
        };
        out.write("\r\n".getBytes("utf-8"));
    }

    /**
     *  write bytes to client directly without buffer
     */
    public void sendStaticResource(String path){
        File file=new File(path);

        if(!file.exists()){
            new DefaultErrorHandler().handle(request, this, 404);
            return;
        }
        long length=file.length();
        long lastModified=file.lastModified();
        setLongHeader("Content-Length",length);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        String dateString = formatter.format(date);
        setHeader("Last-Modified",dateString);
        try {
            String contentType = Files.probeContentType(Paths.get(path));
             if(contentType==null) {
                Tika tika = new Tika();
                contentType = tika.detect(file);
            }
            if(contentType!=null) {
                 if(("text/html").equalsIgnoreCase(contentType)){
                     contentType+=";charset=UTF-8";
                 }
                setHeader("Content-Type", contentType);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileInputStream in = new FileInputStream(file);) {
            committed=true;
            sendStatusCode();
            sendHeaders();
            in.transferTo(out);
        }  catch (IOException e) {
            new DefaultErrorHandler().handle(request, this, 500);
        }
    }
    public void setLongHeader(String name, long value){
        setHeader(name,value+"");
    }
}
