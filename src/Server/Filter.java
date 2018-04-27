package Server;

public interface Filter {
    void doFilter(HttpRequest request, HttpResponse response, FilterChain chain);
}

