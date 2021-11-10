package no.kristiania.http.controllers;

import no.kristiania.dao.QuestionDao;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;
import no.kristiania.dao.model.Question;
import no.kristiania.http.ResponseCode;

import java.sql.SQLException;
import java.util.List;

public class QuestionsController implements Controller {

    private QuestionDao dao;

    public QuestionsController(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException {

        if (request.getMethod() == HttpMethod.GET) {
            return get(request);
        } else if (request.getMethod() == HttpMethod.POST) {
            return post(request);
        }

        return new HttpResponse(ResponseCode.ERROR, "Internal Server Error", "text/plain");
    }

    private HttpResponse get(HttpRequest request) throws SQLException {

        List<Question> list = dao.retrieveAll(dao.getRetrieveAllString());
        StringBuilder result = new StringBuilder();
        for (var question : list) {
            result.append("<h3>").append(question.getQuestionTitle()).append("</h3>");
            result.append("<p>").append(question.getQuestionText()).append("</p>");
        }

        return new HttpResponse(ResponseCode.OK, result.toString(), "text/html");
    }

    private HttpResponse post(HttpRequest request) {

        // TODO: finish implementing method

        HttpResponse response = new HttpResponse(ResponseCode.SEE_OTHER, "Redirecting", "text/plain");
        response.addHeader("Location", request.getPath());
        return response;
    }
}
