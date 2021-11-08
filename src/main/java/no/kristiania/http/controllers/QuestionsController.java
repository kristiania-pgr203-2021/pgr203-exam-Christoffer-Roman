package no.kristiania.http.controllers;

import no.kristiania.dao.DbObject;
import no.kristiania.dao.QuestionDao;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;
import no.kristiania.model.Question;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class QuestionsController implements Controller {
    private QuestionDao dao;

    public QuestionsController(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException {

        List<DbObject> list = dao.retrieveAll(dao.getRetrieveAllStatement());
        StringBuilder result = new StringBuilder();
        for (var obj : list) {
            Question question = (Question) obj;
            result.append("<h3>").append(question.getQuestion()).append("</h3>");

        }
        System.out.println(result);
        return new HttpResponse("HTTP/1.1 200 OK", result.toString(), "text/html");

    }
}
