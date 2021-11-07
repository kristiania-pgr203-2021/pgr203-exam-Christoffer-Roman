package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;


public class HttpWorker implements Runnable {
    private final Socket socket;
    private HashMap<String, String> headers;
    private final HttpServer server;

    public HttpWorker(HttpServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            String[] requestLine = HttpMessage.readLine(socket).split(" ", 3);
            headers = HttpMessage.readInputHeaders(socket);
            String path = requestLine[1];
            StringBuilder responseMessage;
            String statusCode, query = null, contentType = "text/plain";

            // Checking if path contains queries
            int questionPos = path.indexOf("?");
            if (questionPos != -1) {
                query = path.substring(questionPos + 1);
                path = path.substring(0, questionPos);
            }
            // If no path is specified, just send to /index.html
            if (path.equals("/")) path = "/index.html";

            if(server.getRootPath() != null && Files.exists(server.getRootPath().resolve(path.substring(1)))){
                responseMessage = new StringBuilder(Files.readString(server.getRootPath().resolve(path.substring(1))));
                contentType = getContentType(path);
                statusCode = "200 OK";

            } else {
                responseMessage = new StringBuilder("NOT FOUND!");
                statusCode = "404 NOT FOUND";
            }

            socket.getOutputStream().write(("HTTP/1.1 " + statusCode + "\r\n" +
                    "Content-Length: " + responseMessage.length() + "\r\n" +
                    "Content-Type: " + contentType + ";charset=utf-8\r\n" +
                    "\r\n" +
                    responseMessage + "\n").getBytes());

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getContentType(String path) {
        String contentType;
        contentType = "text/plain";
        if (path.endsWith(".html")){
            contentType = "text/html";
        }
        return contentType;
    }
}