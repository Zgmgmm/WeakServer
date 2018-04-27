package Handler;

import Server.AbstractHttpHandler;
import Server.HttpRequest;
import Server.HttpResponse;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleHandler extends AbstractHttpHandler {
    private ArrayList words;
    private Template t;
    {
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        t = ve.getTemplate("web/post.html");
        words = new ArrayList();
    }
    public void doHEAD(HttpRequest request, HttpResponse response) {
        VelocityContext context = new VelocityContext();
        context.put("words", words);
        String html=render(context);
        response.setHeader("Content-Length", html.getBytes().length+"");
    }
    public void doGET(HttpRequest request, HttpResponse response) {
        VelocityContext context = new VelocityContext();
        context.put("words", words);
        String html=render(context);
        response.getWriter().println(html);
    }

    public void doPOST(HttpRequest request, HttpResponse response) {
        String word = request.getParam("word");
        if(word!=null&&!word.isEmpty())
            words.add(word);
       doGET(request,response);
    }
    public void doPUT(HttpRequest request, HttpResponse response) {
        InputStream in=request.getInputStream();
        String path=request.getPath().substring(1);
        int pos=path.lastIndexOf("/");
        String filename=path.substring(pos+1);
        try {
            File file=new File("put",filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out=new FileOutputStream(file);
            in.transferTo(out);
            response.setCode(201);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String render(VelocityContext context) {
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }
            }
