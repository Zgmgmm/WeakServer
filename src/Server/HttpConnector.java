package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpConnector implements Runnable{
    final public static int DEFAULT_PORT=80;
    final public static String DEFAULT_HOST="127.0.0.1";
    private String scheme="http";
    private ServerSocket serverSocket;
    private Stack<HttpProcessor> processors;
    private int minProcessors=5;
    private int maxProcessors=20;
    Executor executor;
    private  boolean stopped;
    public String getScheme(){
        return scheme;
    }
    public HttpConnector() throws IOException {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }
    public HttpConnector(int port) throws IOException {
        this(DEFAULT_HOST,port);
    }

    public HttpConnector(String host, int port) throws IOException {
        serverSocket=new ServerSocket(port, 50,  InetAddress.getByName(host));
    }

    @Override
    public void run() {
        processors= new Stack<>();
        executor= Executors.newCachedThreadPool();
        while(!stopped) {
            try {
                Socket socket=serverSocket.accept();
                executor.execute(new HttpProcessor(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(){
        Thread thread=new Thread(this);
        thread.start();
    }
    public void stop(){
        stopped=true;
    }
}
