package no.kristiania.http.controllers;

import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.model.Answer;
import no.kristiania.dao.model.Question;
import no.kristiania.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class AnswersController implements Controller {

    private AnswerDao dao;
    private HashMap<String, String> queryParameters;

    public AnswersController(AnswerDao dao) {
        this.dao = dao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {

        if (request.getMethod().equals(HttpMethod.GET)) {
            return get(request);
        } else if (request.getMethod().equals(HttpMethod.POST)) {
            return post(request);
        }

        return new HttpResponse(ResponseCode.METHOD_NOT_ALLOWED, "Method Not Allowed", "text/plain");
    }

    private HttpResponse get(HttpRequest request) throws SQLException {

        List<Answer> list = dao.retrieveByQuestionId(Long.parseLong(queryParameters.get("id")), dao.getRetrieveByIdString());

        StringBuilder result = new StringBuilder();
        result.append("<ul>");

        for (var a : list) {
            result.append("<li>").append(a.getAnswerText()).append("</li>");
        }

        result.append("</ul>");

        return new HttpResponse(ResponseCode.OK, result.toString(), "text/html");
    }

    private HttpResponse post(HttpRequest request) throws SQLException {

        Answer a  = new Answer(queryParameters.get("answerText"), Long.parseLong(queryParameters.get("questionId")));
        dao.save(a, dao.getSaveString());

        HttpResponse response = new HttpResponse(ResponseCode.SEE_OTHER, "Redirecting", "text/plain");
        response.addHeader("Location", "/allQuestions.html");
        return response;
    }

    public void setQueryParameters(String queryString) {
        queryParameters = null; // Clearing if there was old queryParameters
        if (queryString.equals("") || queryString == null) return;
        queryParameters = HttpRequest.parseQueryParameters(queryString);
    }
}
