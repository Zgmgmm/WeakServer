package Server;

public abstract class AbstractHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        String method=request.getMethod();
        if("GET".equals(method)){
            doGET(request,response);
        }else if("POST".equals(method)){
            doPOST(request,response);
        }else if("HEAD".equals(method)){
            doHEAD(request,response);
        }else if("PUT".equals(method)){
            doPUT(request,response);
        }
    }
    public void doGET(HttpRequest request, HttpResponse response){};
    public void doPOST(HttpRequest request, HttpResponse response){
        doGET(request,response);
    }
    public void doHEAD(HttpRequest request, HttpResponse response){
        doGET(request,response);
    }
    public void doPUT(HttpRequest request, HttpResponse response){
        doGET(request,response);
    }
}
