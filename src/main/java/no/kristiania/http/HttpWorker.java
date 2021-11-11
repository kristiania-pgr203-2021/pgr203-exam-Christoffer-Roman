package no.kristiania.http;

import no.kristiania.http.controllers.Controller;
import no.kristiania.http.controllers.QuestionsController;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
            HttpMethod method = HttpMethod.valueOf(requestLine[0].toUpperCase());
            String path = requestLine[1];
            String queryString = "";

            // Checking if path contains queries
            int questionPos = path.indexOf("?");
            if (questionPos != -1) {
                queryString = path.substring(questionPos + 1);
                path = path.substring(0, questionPos);
            }
            // If no path is specified, just send to /index.html
            if (path.equals("/")) path = "/index.html";

            Controller controller;
            if ((controller = server.getController(path)) != null) {
                if (method == HttpMethod.POST) {
                    queryString = HttpMessage.readBytes(socket, Integer.parseInt(headers.get("Content-Length")));
                }
                if (controller instanceof QuestionsController) {
                    ((QuestionsController) controller).setQueryParameters(queryString);
                }

                HttpResponse httpResponse = controller.handle(new HttpRequest(method, path));
                write(httpResponse, socket);
            } else {
                write(new HttpResponse(ResponseCode.NOT_FOUND, ResponseCode.NOT_FOUND.toString(), "text/plain"), socket);
            }


        } catch (IOException | SQLException e) {
            try {
                write(new HttpResponse(ResponseCode.ERROR, ResponseCode.ERROR.toString(), "text/plain"), socket);
            } catch (IOException ex) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void write(HttpResponse response, Socket socket) throws IOException {
        socket.getOutputStream().write(("HTTP/1.1 " + response.getResponseCode() + "\r\n" +
                "Content-Length: " +
                response.getResponseBody().getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "Content-Type: " + response.getContentType() + ";charset=utf-8\r\n" +
                "Location: " + response.getHeader("Location") + "\r\n" +
                "Connection: keep-alive\r\n" +
                "\r\n" +
                response.getResponseBody()).getBytes(StandardCharsets.UTF_8));

        socket.close();
    }
}