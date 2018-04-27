package Server;
public interface ErrorHandler{
    public void handle(HttpRequest request, HttpResponse response, int errorCode);
}
