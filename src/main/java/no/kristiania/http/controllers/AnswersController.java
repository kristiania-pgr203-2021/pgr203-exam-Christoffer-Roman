package no.kristiania.http.controllers;

import no.kristiania.Main;
import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.model.Answer;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;
import no.kristiania.http.ResponseCode;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class AnswersController implements Controller {

    public static final String PATH = "/api/answers";
    private AnswerDao dao;
    private HashMap<String, String> queryParameters;

    public AnswersController(AnswerDao dao) {
        this.dao = dao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException {

        if (request.getMethod().equals(HttpMethod.GET)) {
            Main.logger.info("All answers to one question returned to client");
            return get();
        } else if (request.getMethod().equals(HttpMethod.POST)) {
            Main.logger.info("Answer saved, redirecting client");
            return post();
        }

        Main.logger.info("Client request method not allowed, returning 405");
        return new HttpResponse(ResponseCode.METHOD_NOT_ALLOWED, "Method Not Allowed", "text/plain");
    }

    private HttpResponse get() throws SQLException {

        List<Answer> list = dao.retrieveByQuestionId(Long.parseLong(queryParameters.get("id")), dao.getRetrieveByQuestionIdString());

        StringBuilder result = new StringBuilder();
        result.append("<ul>");

        for (var a : list) {
            result.append("<li>").append(a.getAnswerText()).append("</li>");
        }

        result.append("</ul>");

        return new HttpResponse(ResponseCode.OK, result.toString(), "text/html");
    }

    private HttpResponse post() throws SQLException {

        Answer a  = new Answer(queryParameters.get("answerText"), Long.parseLong(queryParameters.get("questionId")));
        dao.save(a, dao.getSaveString());

        HttpResponse response = new HttpResponse(ResponseCode.SEE_OTHER, "Redirecting", "text/plain");
        response.addHeader("Location", "/allQuestions.html");
        return response;
    }

    public void setQueryParameters(String queryString) {
        queryParameters = HttpRequest.parseQueryParameters(queryString);
    }
}
