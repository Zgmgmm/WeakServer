package Handler;

import Server.AbstractHttpHandler;
import Server.HttpRequest;
import Server.HttpResponse;


/**
 * handle a request sending static resources
 */
public class DefaultHandler extends AbstractHttpHandler {
    @Override
    public void doGET(HttpRequest request, HttpResponse response) {
        String path = request.getPath().substring(1);
        response.sendStaticResource(path);
    }
}
