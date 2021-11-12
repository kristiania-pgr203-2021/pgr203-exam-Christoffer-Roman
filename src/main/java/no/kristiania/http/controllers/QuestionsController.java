package no.kristiania.http.controllers;

import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.model.Question;
import no.kristiania.http.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class QuestionsController implements Controller {

    public static final String PATH = "/api/questions";
    private QuestionDao dao;
    private HashMap<String, String> queryParameters;

    public QuestionsController(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException {

        if (request.getMethod().equals(HttpMethod.GET)) {
            return get(request);
        } else if (request.getMethod().equals(HttpMethod.POST)) {
            if (queryParameters.get("dbAction").equals("update")) return patch(request);
            return post(request);
        }

        return new HttpResponse(ResponseCode.METHOD_NOT_ALLOWED, "Method Not Allowed", "text/plain");
    }

    private HttpResponse get(HttpRequest request) throws SQLException {

        StringBuilder result = new StringBuilder();

        if (queryParameters == null) {

            List<Question> list = dao.retrieveAll(dao.getRetrieveAllString());

            for (var q : list) {
                result.append("<h3>").append(q.getQuestionTitle()).append("</h3>");
                result.append("<p>").append(q.getQuestionText()).append("</p>");
                result.append("<p><a href='/addAnswer.html?id=").append(q.getId())
                        .append("&questionTitle=").append(q.getQuestionTitle())
                        .append("&questionText=").append(q.getQuestionText())
                        .append("'>Answer question</a></p>");
                result.append("<p><a href='/allAnswers.html?id=").append(q.getId())
                        .append("&questionTitle=").append(q.getQuestionTitle())
                        .append("&questionText=").append(q.getQuestionText())
                        .append("'>See all answers</a></p>");
                result.append("<p><a href='/editQuestion.html?id=").append(q.getId())
                        .append("&questionTitle=").append(HttpMessage.encodeToQuery(q.getQuestionTitle()))
                        .append("&questionText=").append(HttpMessage.encodeToQuery(q.getQuestionText()))
                        .append("'>Edit question</a></p>");
            }
        } else {
            if (queryParameters.size() > 1) {
                result.append("<input id='id' type='hidden' name='id' value='")
                        .append(queryParameters.get("id"))
                        .append("'><p><label>Title: <input type='text' name='questionTitle' placeholder='")
                        .append(HttpMessage.decodeQuery(queryParameters.get("questionTitle")))
                        .append("'></label></p><p><label>Question: <input type='text' name='questionText' placeholder='")
                        .append(HttpMessage.decodeQuery(queryParameters.get("questionText")))
                        .append("'></label></p>");
            } else {
                result
                        .append("<input type='hidden' name='questionId' value='")
                        .append(queryParameters.get("questionId"))
                        .append("'>");
            }
        }

        return new HttpResponse(ResponseCode.OK, result.toString(), "text/html");
    }

    private HttpResponse post(HttpRequest request) throws SQLException {

        Question q = new Question(queryParameters.get("questionTitle"), queryParameters.get("questionText"));
        dao.save(q, dao.getSaveString());

        HttpResponse response = new HttpResponse(ResponseCode.SEE_OTHER, "Redirecting", "text/plain");
        response.addHeader("Location", "/allQuestions.html");
        return response;
    }

    private HttpResponse patch(HttpRequest request) throws SQLException {

        Question q = new Question(queryParameters.get("questionTitle"), queryParameters.get("questionText"));
        q.setId(Long.parseLong(queryParameters.get("id")));
        dao.update(q, dao.getUpdateString());

        HttpResponse response = new HttpResponse(ResponseCode.SEE_OTHER, "Redirecting", "text/plain");
        response.addHeader("Location", "/allQuestions.html");
        return response;
    }

    public void setQueryParameters(String queryString) {
        queryParameters = null; // Clearing if there was old queryParameters
        queryParameters = HttpRequest.parseQueryParameters(queryString);
    }
}