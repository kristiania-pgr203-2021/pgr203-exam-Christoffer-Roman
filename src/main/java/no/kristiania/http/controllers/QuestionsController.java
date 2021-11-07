package no.kristiania.http.controllers;

import no.kristiania.dao.*;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpRequest;

import java.sql.SQLException;

public class QuestionsController implements Controller {
    private QuestionDao dao;

    public QuestionsController(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public HttpMessage handle(HttpRequest request) throws SQLException {
        return null;
    }
}
