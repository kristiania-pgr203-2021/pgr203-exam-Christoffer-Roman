package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class HttpMessage {

    private String path;
    private HashMap<String, String> headers;

    public HttpMessage(String path) {
        this.path = path;
    }

    public HttpMessage() {
    }

    public static HashMap<String, String> readInputHeaders(Socket socket) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        String[] splitted;
        String line = readLine(socket), key, value;

        while (!line.isEmpty()) {
            splitted = line.split(":");
            key = splitted[0];
            value = splitted[1].trim();
            headers.put(key, value);
            line = readLine(socket);
        }
        return headers;
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

    public static String readLine(Socket socket) throws IOException{
        StringBuilder result = new StringBuilder();
        int c;
        while((c = socket.getInputStream().read()) != '\r'){
            result.append((char)c);
        }
        int expectedNewLine = socket.getInputStream().read();
        assert expectedNewLine == '\n';
        return result.toString();
    }

    public static String readBytes(Socket socket, int contentLength) throws IOException{
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            result.append((char)socket.getInputStream().read());
        }
        return result.toString();
    }

    public static String getContentType(String path) {
        String contentType;
        contentType = "text/plain";
        if (path.endsWith(".html")){
            contentType = "text/html";
        } else if (path.endsWith(".css")){
            contentType = "text/css";
        }
        return contentType;
    }

    public String getPath() {
        return path;
    }
}
