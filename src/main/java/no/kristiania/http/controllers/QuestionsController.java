package no.kristiania.http.controllers;

import no.kristiania.dao.QuestionDao;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;
import no.kristiania.dao.model.Question;

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

        return new HttpResponse("HTTP/1.1 500 Internal Server Error", "Internal Server Error", "text/plain");
    }

    private HttpResponse get(HttpRequest request) throws SQLException {

        List<Question> list = dao.retrieveAll(dao.getRetrieveAllString());
        StringBuilder result = new StringBuilder();
        for (var obj : list) {
            Question question = obj;
            result.append("<h3>").append(question.getQuestionTitle()).append("</h3>");
            result.append("<p>").append(question.getQuestionText()).append("</p>");
        }
        System.out.println(result);
        return new HttpResponse("HTTP/1.1 200 OK", result.toString(), "text/html");
    }

    private HttpResponse post(HttpRequest request) {

        // TODO: finish implementing method

        HttpResponse response = new HttpResponse("HTTP/1.1 303 See Other", "Redirecting", "text/plain");
        response.addHeader("Location", request.getPath());
        return response;
    }
}
