package Filter;

import Server.Filter;
import Server.FilterChain;
import Server.HttpRequest;
import Server.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

public class GzipFilter implements Filter {
    class ResponseWrapper extends HttpResponse{
        GZIPOutputStream gzipOut;
        public ResponseWrapper(OutputStream out) {
            super(out);
        }
        @Override
        public OutputStream getOutputStream(){
            if(gzipOut==null) {
                try {
                    gzipOut=new GZIPOutputStream(super.getOutputStream(),true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return gzipOut;
        }
        @Override
        public PrintWriter getWriter(){
            if (printWriter != null)
                return printWriter;
            if (buff != null && !usingWriter)
                throw new RuntimeException("using stream!");
            printWriter = new PrintWriter(getOutputStream(),true);
            usingWriter = true;
            return printWriter;
        }
        @Override
        public void finishResponse(){
            try {
                gzipOut.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.finishResponse();
        }


    }
    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
        ResponseWrapper wrapper=null;
        wrapper=new ResponseWrapper(response.getOutputStream());
        chain.doFilter(request,wrapper);
        response.setHeader("Content-Encoding","gzip");
    }
}
