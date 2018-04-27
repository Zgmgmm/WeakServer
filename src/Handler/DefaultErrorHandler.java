package Handler;

import Server.ErrorHandler;
import Server.HttpRequest;
import Server.HttpResponse;

import java.io.PrintWriter;

public class DefaultErrorHandler implements ErrorHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response, int errorCode) {
        response.setCode(errorCode);
        PrintWriter writer=null;
        writer=response.getWriter();
        writer.println("not found!");
    }
}
