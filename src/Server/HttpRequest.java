package Server;

import javax.servlet.http.Cookie;
import java.io.InputStream;
import java.util.*;

public class HttpRequest{
    protected String method;
    protected String path;
    protected String version;
    protected Map<String, String> headers=new HashMap<>();
    protected Map<String, String> params=new HashMap<>();
    protected List<Cookie> cookies;
    protected InputStream in;
    protected String requestSessionId;

    public HttpRequest(InputStream in) {
        this.in = in;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public InputStream getInputStream() {
        return in;
    }

    public void setParam(String name, String value) {
        params.put(name, value);
    }

    public String getParam(String name) {
        return params.get(name);
    }

    public String getRequestSessionId(){
        return requestSessionId;
    }
    public void setRequestSessionId(String requestSessionId){
        this.requestSessionId=requestSessionId;
    }

    public void addCookies(Cookie cookie){
        cookies.add(cookie);
    }
    public void addCookies(Collection<Cookie> cookies){
        if(cookies==null)
            return;
        this.cookies.addAll(cookies);
    }

    public void setParameter(String name, String value){
        params.put(name,value);
    }
}
