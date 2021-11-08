package no.kristiania.http.controllers;

import no.kristiania.dao.QuestionDao;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;
import no.kristiania.http.HttpServer;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class QuestionsController extends Controller {
    private QuestionDao dao;

    public QuestionsController(QuestionDao dao, HttpServer server) {
        super(server);
        this.dao = dao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws IOException {
        String responseMessage = Files.readString(server.getRootPath().resolve(request.getPath().substring(1)));
        String contentType = HttpMessage.getContentType(request.getPath());
        return new HttpResponse("HTTP/1.1 200 OK", responseMessage, contentType);
    }
}
