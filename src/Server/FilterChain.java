package Server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
public class FilterChain {
    protected List<Filter> filters=new ArrayList<>();
    protected Iterator<Filter> iterator;
    protected HttpHandler handler;
    protected int curPos=0;
    public FilterChain(HttpHandler handler){
        this.handler=handler;
    }
    public FilterChain append(Filter filter){
        filters.add(filter);
        return this;
    }
    public FilterChain appendAll(Collection<Filter> filters){
        this.filters.addAll(filters);
        return this;
    }
    public void doFilter(HttpRequest request, HttpResponse response){
        if (this.iterator == null)
            this.iterator = filters.iterator();
        if(iterator.hasNext()){
            iterator.next().doFilter(request,response,this);
        }else{
            handler.handle(request,response);
        }
    }

}
