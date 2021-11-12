package no.kristiania.http;

import java.util.HashMap;

public class HttpRequest extends HttpMessage {

    private String path;
    private HttpMethod method;
    private String host;
    private String body;

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    private String queryString;

    public HttpRequest(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public HttpRequest(HttpMethod method, String path, String host) {
        this.method = method;
        this.path = path;
        this.host = host;
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

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        if (method.equals(HttpMethod.GET)) {
            result.append(method.name() + " " + path+queryString + " HTTP/1.1\r\n");
            result.append("Host: " + host + "\r\nConnection: keep-alive\r\n\r\n");

            return result.toString();
        }

        result.append(method.name() + " " + path + " HTTP/1.1\r\n");
        result.append("Content-Length: ").append(queryString.length()).append("\r\n");
        result.append("Host: " + host + "\r\nConnection: keep-alive\r\n\r\n");
        result.append(queryString);

        return result.toString();
    }
}
