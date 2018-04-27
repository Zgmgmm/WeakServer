package Handler;

import Server.AbstractHttpHandler;
import Server.HttpRequest;
import Server.HttpResponse;

import java.io.File;

public class DownloadHandler extends AbstractHttpHandler{
    @Override
    public void doGET(HttpRequest request, HttpResponse response) {
        String path=request.getPath().substring(1);
        File file=new File(path);
        response.setHeader("Content-Disposition","attachment; filename=" + file.getName());
        response.sendStaticResource(path);
    }
}
