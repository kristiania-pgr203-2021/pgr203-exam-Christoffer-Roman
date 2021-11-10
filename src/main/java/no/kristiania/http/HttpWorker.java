package no.kristiania.http;

import no.kristiania.http.controllers.Controller;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
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
            String method = requestLine[0];
            String path = requestLine[1], query;

            // Checking if path contains queries
            int questionPos = path.indexOf("?");
            if (questionPos != -1) {
                query = path.substring(questionPos + 1);
                path = path.substring(0, questionPos);
            }
            // If no path is specified, just send to /index.html
            if (path.equals("/")) path = "/index.html";

            Controller controller;
            if ((controller = server.getController(path)) != null) {
                HttpResponse httpResponse = controller.handle(new HttpRequest(method, path));
                write(httpResponse, socket);
            }
            else {
                write(new HttpResponse("HTTP/1.1 404 NOT FOUND", "NOT FOUND!", "text/plain"), socket);
            }


        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void write(HttpResponse response, Socket socket) throws IOException {
        socket.getOutputStream().write((response.getResponseLine() + "\r\n" +
                "Content-Length: " + response.getResponseBody().getBytes().length + "\r\n" +
                "Content-Type: " + response.getContentType() + ";charset=utf-8\r\n" +
                "\r\n" +
                response.getResponseBody() + "\n").getBytes());
        socket.close();
    }
}