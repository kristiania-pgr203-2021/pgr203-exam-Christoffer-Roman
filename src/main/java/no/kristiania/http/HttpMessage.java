package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class HttpMessage {

    protected HashMap<String, String> headers = new HashMap<>();

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

    public static HashMap<String, String> readInputHeaders(Socket socket) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        String[] split;
        String line = readLine(socket), key, value;

        while (!line.isEmpty()) {
            split = line.split(":");
            key = split[0];
            value = split[1].trim();
            headers.put(key, value);
            line = readLine(socket);
        }
        return headers;
    }
}
