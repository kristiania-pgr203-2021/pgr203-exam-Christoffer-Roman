package no.kristiania.http.controllers;

import no.kristiania.dao.QuestionDao;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;
import no.kristiania.dao.model.Question;
import no.kristiania.http.ResponseCode;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class QuestionsController implements Controller {

    private QuestionDao dao;

    public QuestionsController(QuestionDao dao) {
        this.dao = dao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException {

        if (request.getMethod().equals(HttpMethod.GET)) {
            return get();
        } else if (request.getMethod().equals(HttpMethod.POST)) {
            return post(request);
        } else if (request.getMethod().equals(HttpMethod.PATCH)) {
            return patch(request);
        }

        return new HttpResponse(ResponseCode.METHOD_NOT_ALLOWED, "Method Not Allowed", "text/plain");
    }

    private HttpResponse get() throws SQLException {

        List<Question> list = dao.retrieveAll(dao.getRetrieveAllString());

        StringBuilder result = new StringBuilder();

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
                    .append("&questionTitle=").append(q.getQuestionTitle())
                    .append("&questionText=").append(q.getQuestionText())
                    .append("'>Edit question</a></p>");
        }

        return new HttpResponse(ResponseCode.OK, result.toString(), "text/html");
    }

    private HttpResponse post(HttpRequest request) throws SQLException {

        HashMap<String, String> qp = request.parseQueryParameters();

        Question q = new Question(qp.get("questionTitle"), qp.get("questionText"));
        dao.save(q, dao.getSaveString());

        HttpResponse response = new HttpResponse(ResponseCode.SEE_OTHER, "Redirecting", "text/plain");
        response.addHeader("Location", "/allQuestions.html");
        return response;
    }

    private HttpResponse patch(HttpRequest request) throws SQLException {

        HashMap<String, String> qp = request.parseQueryParameters();

        Question q = new Question(qp.get("questionTitle"), qp.get("questionText"));
        q.setId(Long.parseLong(qp.get("id")));
        dao.update(q, dao.getUpdateString());

        HttpResponse response = new HttpResponse(ResponseCode.SEE_OTHER, "Redirecting", "text/plain");
        response.addHeader("Location", "/allQuestions.html");
        return response;
    }
}
