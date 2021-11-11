package no.kristiania.http.controllers;

import no.kristiania.dao.AnswerDao;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class AnswersController implements Controller {

    private AnswerDao dao;
    private HashMap<String, String> queryParameters;

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        // TODO: implement method
        return null;
    }

    private HttpResponse get(HttpRequest request) {
        // TODO: implement method
        return null;
    }

    private HttpResponse post(HttpRequest request) {
        // TODO: implement method
        return null;
    }
}
