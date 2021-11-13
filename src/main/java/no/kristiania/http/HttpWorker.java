package no.kristiania.http;

import no.kristiania.Main;
import no.kristiania.http.controllers.AnswerAlternativesController;
import no.kristiania.http.controllers.AnswersController;
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
            String requestLine = HttpMessage.readLine(socket);
            headers = HttpMessage.readInputHeaders(socket);
            HttpMethod method = HttpMethod.valueOf(requestLine.substring(0, requestLine.indexOf(" ")).toUpperCase());
            String path, queryString = null;

            // Checking if path contains queries
            int questionPos = requestLine.indexOf("?");
            if (questionPos != -1) {
                queryString = requestLine.substring(questionPos + 1, requestLine.lastIndexOf(" "));
                path = requestLine.substring(requestLine.indexOf(" ") + 1, questionPos);
            } else {
                path = requestLine.split(" ")[1];
            }
            // If no path is specified, just send to /index.html
            if (path.equals("/")) path = "/index.html";

            Controller controller;
            if ((controller = server.getController(path)) != null) {
                if (method == HttpMethod.POST) {
                    queryString = HttpMessage.readBytes(socket, Integer.parseInt(headers.get("Content-Length")));
                }
                setQueriesOnControllers(queryString, controller);

                HttpResponse httpResponse = controller.handle(new HttpRequest(method, path));
                write(httpResponse, socket);
            } else {
                Main.logger.info("Client request path not found, returning 404");
                write(new HttpResponse(ResponseCode.NOT_FOUND, ResponseCode.NOT_FOUND.toString(), "text/plain"), socket);
            }


        } catch (IOException | SQLException | RuntimeException e) {
            try {
                write(new HttpResponse(ResponseCode.ERROR, ResponseCode.ERROR.toString(), "text/plain"), socket);
            } catch (IOException ex) {
                System.out.println(e.getMessage());
            } finally {
                e.printStackTrace();
            }
        }
    }

    private void setQueriesOnControllers(String queryString, Controller controller) {
        if (controller instanceof QuestionsController) {
            ((QuestionsController) controller).setQueryParameters(queryString);
        }
        if (controller instanceof AnswersController) {
            ((AnswersController) controller).setQueryParameters(queryString);
        }
        if (controller instanceof AnswerAlternativesController) {
            ((AnswerAlternativesController) controller).setQueryParameters(queryString);
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