package Server;

public interface HttpHandler{
    void handle(HttpRequest request, HttpResponse response);
}
