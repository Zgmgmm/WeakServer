package Filter;

import Server.Filter;
import Server.FilterChain;
import Server.HttpRequest;
import Server.HttpResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFilter implements Filter{
    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
        chain.doFilter(request,response);
        response.setHeader("Date", getFormatDate());
    }
    public String getFormatDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        String dateString = formatter.format(date);
        return dateString;
    }
}
