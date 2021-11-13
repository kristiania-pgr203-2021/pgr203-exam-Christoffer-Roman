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
    private AnswerAlternativeDao alternativeDao = new AnswerAlternativeDao(Main.getDataSource());
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

        Main.logger.info("Client request method not allowed, returning 405");
        return new HttpResponse(ResponseCode.METHOD_NOT_ALLOWED, "Method Not Allowed", "text/plain");
    }

    private HttpResponse get() throws SQLException {
        StringBuilder result = new StringBuilder();
        if (queryParameters == null) { // means it is a regular get, so we should get and format all
            List<Question> list = dao.retrieveAll(dao.getRetrieveAllString());
            for (var q : list) {
                getAllText(result, q);
            }
            Main.logger.info("All questions returned to client");
        } else {
            if (queryParameters.get("questionType").equals("0")) {
                getRegular(result);
            } else if (queryParameters.get("questionType").equals("1")) {
                getMultiple(result);
            } else {
                result.append("<input type='hidden' name='questionId' value='")
                        .append(queryParameters.get("questionId"))
                        .append("'>");
            }
            Main.logger.info("One question returned to client");
        }

        return new HttpResponse(ResponseCode.OK, result.toString(), "text/html");
    }

    private void getMultiple(StringBuilder result) throws SQLException {
        getRegular(result);
        List<AnswerAlternative> alternatives = alternativeDao.retrieveAllById(
                Long.parseLong(queryParameters.get("id")),
                alternativeDao.getRetrieveByQuestionIdString());
        for (int i = 1; i <= alternatives.size(); i++) {
            result.append("<label>Answer")
                    .append(i)
                    .append("</label><input name='answer")
                    .append(alternatives.get(i - 1).getId())
                    .append("' type='text' placeholder='")
                    .append(alternatives.get(i - 1).getAnswerText())
                    .append("'>");
        }

    }

    private void getRegular(StringBuilder result) {
        result.append("<input id='id' type='hidden' name='id' value='")
                .append(queryParameters.get("id"))
                .append("'><p><label>Title: <input type='text' name='questionTitle' placeholder='")
                .append(queryParameters.get("questionTitle"))
                .append("'></label></p><p><label>Question: <input type='text' name='questionText' placeholder='")
                .append(queryParameters.get("questionText"))
                .append("'></label></p>");
    }

    private void getAllText(StringBuilder result, Question q) {
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

    private HttpResponse post() throws SQLException {
        Question q = new Question(queryParameters.get("questionTitle"), queryParameters.get("questionText"));
        dao.save(q, dao.getSaveString());

        return redirectResponse("/allQuestions.html");
    }


    private HttpResponse postMultipleAnswers() throws SQLException {
        Question question = new Question(queryParameters.get("questionTitle"),
                queryParameters.get("questionText"), Question.QuestionType.MULTIPLE_ANSWERS);
        dao.save(question, dao.getSaveString());

        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            if (entry.getKey().contains("answer")) {
                AnswerAlternative answerAlternative = new AnswerAlternative(entry.getValue(), question.getId());
                alternativeDao.save(answerAlternative, alternativeDao.saveString);
            }
        }

        Main.logger.info("Question saved, redirecting client");
        return redirectResponse("/allQuestions.html");
    }

    private HttpResponse patch() throws SQLException {

        Question fromDb = dao.retrieveById(Long.parseLong(queryParameters.get("id")), dao.getRetrieveByQuestionIdString());
        String questionTitle = queryParameters.get("questionTitle");
        String questionText = queryParameters.get("questionText");
        if (!questionTitle.equals("")) {
            fromDb.setQuestionTitle(questionTitle);
        }
        if (!questionText.equals("")) {
            fromDb.setQuestionText(questionText);
        }

        dao.update(fromDb, dao.getUpdateString());

        for (var entry : queryParameters.entrySet()) {
            if (entry.getKey().contains("answer")) {
                if (entry.getValue().equals("")) continue;
                long id = Long.parseLong(entry.getKey().replace("answer", ""));
                var alternative = alternativeDao.retrieveById(id, alternativeDao.retrieveByIdString);
                alternative.setAnswerText(entry.getValue());
                alternativeDao.update(alternative, alternativeDao.updateString);
            }
        }

        Main.logger.info("Question updated, redirecting client");
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
