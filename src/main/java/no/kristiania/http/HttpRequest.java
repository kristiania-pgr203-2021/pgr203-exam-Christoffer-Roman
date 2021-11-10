package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class HttpRequest extends HttpMessage {

    private HttpMethod method;

    public HttpRequest(String method, String path) {
        this.method = HttpMethod.valueOf(method);
        this.path = path;
    }

    public static HashMap<String, String> parseQueryParameters(String queryString) {
        if (queryString == null) return null;
        HashMap<String, String> queryParams = new HashMap<>();
        while (queryString.contains("=")) {
            int equalsPos = queryString.indexOf("=");
            String key = queryString.substring(0, equalsPos), value;
            queryString = queryString.substring(equalsPos + 1);
            int andPos = queryString.indexOf("&");

            if (andPos != -1) {
                value = queryString.substring(0, andPos);
                queryString = queryString.substring(andPos + 1);
            } else {
                value = queryString;
            }
            while (key.contains("+")) key = key.replace("+", " ");
            while (value.contains("+")) value = value.replace("+", " ");
            queryParams.put(key, value);
        }

        return queryParams;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
