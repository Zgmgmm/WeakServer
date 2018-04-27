package Handler;

import Server.AbstractHttpHandler;
import Server.HttpRequest;
import Server.HttpResponse;

import java.io.PrintWriter;

public class EchoHandler extends AbstractHttpHandler {
    @Override
    public void doGET(HttpRequest request, HttpResponse response) {
        //PrintWriter out=response.getWriter();
        PrintWriter out=response.getWriter();
        out.println("<pre>");
        request.getHeaders().entrySet().forEach(header->{
            out.println(header.getKey()+": "+header.getValue());
        });
        out.println("</pre>");
        out.flush();

    }
}
