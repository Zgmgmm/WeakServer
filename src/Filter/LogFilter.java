package Filter;

import Server.Filter;
import Server.FilterChain;
import Server.HttpRequest;
import Server.HttpResponse;

import java.util.logging.Logger;

public class LogFilter implements Filter {
    Logger logger=Logger.getLogger(getClass().getCanonicalName());
    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {

    /*   String requestLine=request.getMethod()+" "+request.getPath()+" "+request.getVersion();
        System.out.println();
       /* request.getHeaders().entrySet().forEach(header->{
            System.out.println(header.getKey()+": "+header.getValue());
        });
        System.out.println();
        */
        chain.doFilter(request, response);

    }
}
