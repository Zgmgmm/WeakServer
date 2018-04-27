import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class Client {
    public static  void main(String[] args) throws IOException, InterruptedException {
        testPut();
    }
    public static void testPut() throws IOException, InterruptedException {
        String path="/put/newfile.txt";
/*
        byte[] data=("i am content"+
                "i am content"+
                "i am content"+
                "i am content"+
                "i am content"+
                "i am content").getBytes();

        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Length",data.length+"");
        conn.setDoOutput(true);
        OutputStream out=conn.getOutputStream();
        out.write(data);
        out.flush();
        Thread.sleep(10*1000);
        Socket socket=new Socket("127.0.0.1",80);
        InputStream in=socket.getInputStream();
        in.
 //      conn.disconnect();
 */
    }
}
