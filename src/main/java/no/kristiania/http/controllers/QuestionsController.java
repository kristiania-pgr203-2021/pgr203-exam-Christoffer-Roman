package no.kristiania.http.controllers;

import no.kristiania.Main;
import no.kristiania.dao.AnswerAlternativeDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.model.AnswerAlternative;
import no.kristiania.http.*;
import no.kristiania.dao.model.Question;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            return get();
        } else if (request.getMethod().equals(HttpMethod.POST)) {
            if (queryParameters.get("dbAction").equals("update"))
                return patch();
            if (queryParameters.get("dbAction").equals("multipleAnswers"))
                return postMultipleAnswers();
            return post();
        }

        return new HttpResponse(ResponseCode.METHOD_NOT_ALLOWED, "Method Not Allowed", "text/plain");
    }

    private HttpResponse get() throws SQLException {
        StringBuilder result = new StringBuilder();
        if (queryParameters == null) {

            List<Question> list = dao.retrieveAll(dao.getRetrieveAllString());

            for (var q : list) {
                result.append("<h3>").append(q.getQuestionTitle()).append("</h3>");
                result.append("<p>").append(q.getQuestionText()).append("</p>");
                result.append("<p><a href='/addAnswer.html?id=").append(q.getId())
                        .append("&questionTitle=").append(q.getQuestionTitle())
                        .append("&questionText=").append(q.getQuestionText())
                        .append("&questionType=").append(q.getType().ordinal())
                        .append("'>Answer question</a></p>");
                result.append("<p><a href='/allAnswers.html?id=").append(q.getId())
                        .append("&questionTitle=").append(q.getQuestionTitle())
                        .append("&questionText=").append(q.getQuestionText())
                        .append("&questionType=").append(q.getType().ordinal())
                        .append("'>See all answers</a></p>");
                result.append("<p><a href='/editQuestion.html?id=").append(q.getId())
                        .append("&questionTitle=").append(q.getQuestionTitle())
                        .append("&questionText=").append(q.getQuestionText())
                        .append("&questionType=").append(q.getType().ordinal())
                        .append("'>Edit question</a></p>");
            }
        } else {
            if (queryParameters.size() > 1) {
                result.append("<input id='id' type='hidden' name='id' value='")
                        .append(queryParameters.get("id"))
                        .append("'><p><label>Title: <input type='text' name='questionTitle' placeholder='")
                        .append(queryParameters.get("questionTitle"))
                        .append("'></label></p><p><label>Question: <input type='text' name='questionText' placeholder='")
                        .append(queryParameters.get("questionText"))
                        .append("'></label></p>");
            } else {
                result.append("<input type='hidden' name='questionId' value='")
                        .append(queryParameters.get("questionId"))
                        .append("'>");
            }
        }

        return new HttpResponse(ResponseCode.OK, result.toString(), "text/html");
    }

    private HttpResponse post() throws SQLException {
        Question q = new Question(queryParameters.get("questionTitle"), queryParameters.get("questionText"));
        dao.save(q, dao.getSaveString());

        return redirectResponse("/allQuestions.html");
    }


    private HttpResponse postMultipleAnswers() throws SQLException {
        AnswerAlternativeDao answerAlternativeDao = new AnswerAlternativeDao(Main.getDataSource());
        Question question = new Question(queryParameters.get("questionTitle"),
                queryParameters.get("questionText"), Question.QuestionType.MULTIPLE_ANSWERS);
        dao.save(question, dao.getSaveString());

        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            if (entry.getKey().contains("answer")) {
                AnswerAlternative answerAlternative = new AnswerAlternative(entry.getValue(), question.getId());
                answerAlternativeDao.save(answerAlternative, answerAlternativeDao.saveString);
            }
        }

        return redirectResponse("/allQuestions.html");
    }

    private HttpResponse patch() throws SQLException {

        Question fromDb = dao.retrieveById(Long.parseLong(queryParameters.get("id")), dao.getRetrieveByIdString());
        String questionTitle = queryParameters.get("questionTitle");
        String questionText = queryParameters.get("questionText");
        if (!questionTitle.equals("")) {
            fromDb.setQuestionTitle(questionTitle);
        }
        if (!questionText.equals("")) {
            fromDb.setQuestionText(questionText);
        }

        dao.update(fromDb, dao.getUpdateString());

        return redirectResponse("/allQuestions.html");
    }

    private HttpResponse redirectResponse(String location) {
        HttpResponse response = new HttpResponse(ResponseCode.SEE_OTHER, "Redirecting", "text/plain");
        response.addHeader("Location", location);
        return response;
    }

    public void setQueryParameters(String queryString) {
        queryParameters = HttpRequest.parseQueryParameters(queryString);
    }
}
