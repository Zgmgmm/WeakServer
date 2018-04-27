package Server;

import Handler.DefaultHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.util.Pair;
import util.WildCardMatch;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * map a url to a specified handler and several filters
 */
public class HttpMapper {
    private static HttpMapper instance;
    private List<Pair<String, HttpHandler>> handlerMap = new ArrayList<>();
    private List<Pair<String, Filter>> filterMap = new ArrayList<>();

    private HttpMapper() throws IOException {
        //read map.json
        FileInputStream in = new FileInputStream("web/map.json");
        String json = new String(in.readAllBytes());
        JsonParser parser = new JsonParser();
        JsonArray mappings = parser.parse(json).getAsJsonArray();

        //add handler
        mappings.forEach(element -> {
            try {
                JsonObject mapping= (JsonObject) element;
                if(!mapping.has("handler"))
                    return;
                String handlerName =mapping.get("handler").getAsString();
                Class theClass = Class.forName(handlerName);
                HttpHandler handler = (HttpHandler) theClass.newInstance();
                JsonArray urls = mapping.get("url").getAsJsonArray();
                urls.forEach(element1 -> {
                    String url = element1.getAsString();
                    if (mapping.has("handler")) {
                        handlerMap.add(new Pair<>(url, handler));
                    }
                });
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });
        //add filter
        mappings.forEach(element -> {
            try {
                JsonObject mapping= (JsonObject) element;
                if(!mapping.has("filter"))
                    return;
                String handlerName =mapping.get("filter").getAsString();
                Class theClass = Class.forName(handlerName);
                Filter filter = (Filter) theClass.newInstance();
                JsonArray urls = mapping.get("url").getAsJsonArray();
                urls.forEach(element1 -> {
                    String url = element1.getAsString();
                    if (mapping.has("filter")) {
                        filterMap.add(new Pair<>(url, filter));
                    }
                });
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });
        //default handler
        handlerMap.add(new Pair<>("*", new DefaultHandler()));
    }

    public static HttpMapper getInstance() {
        if (instance == null) {
            try {
                instance = new HttpMapper();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


    public HttpHandler getHandler(String url) {
        for (Pair<String, HttpHandler> pair : handlerMap) {
            if (WildCardMatch.match(url, pair.getKey())) {
                return pair.getValue();
            }
        }
        return null;
    }

    public Collection<Filter> getFilter(String url) {
        List<Filter> filters = new ArrayList<>();
        for (Pair<String, Filter> pair : filterMap) {
            if (WildCardMatch.match(url, pair.getKey())) {
                filters.add(pair.getValue());
            }
        }
        return filters;
    }
}
