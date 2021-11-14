package no.kristiania.http.controllers;

import no.kristiania.Main;
import no.kristiania.dao.AnswerAlternativeDao;
import no.kristiania.dao.model.AnswerAlternative;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpResponse;
import no.kristiania.http.ResponseCode;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AnswerAlternativesController implements Controller{

    public static final String PATH = "/api/questions/alternatives";
    private final AnswerAlternativeDao dao;
    private Map<String, String> queryParameters;

    public AnswerAlternativesController(AnswerAlternativeDao dao) {
        this.dao = dao;
    }


    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException {
        if (request.getMethod().equals(HttpMethod.GET)) {
            return get();
        }
        return new HttpResponse(ResponseCode.ERROR, ResponseCode.ERROR.toString(), "text/plain");
    }

    private HttpResponse get() throws SQLException {
        List<AnswerAlternative> alternatives = dao.retrieveAllById(Long.parseLong(queryParameters.get("id")), dao.getRetrieveByQuestionIdString());
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < alternatives.size(); i++) {

            result.append("<option value='")
                    .append(alternatives.get(i).getAnswerText())
                    .append("'");
                    if (i == 0) {
                        result.append("selected>");
                    } else {
                        result.append(">");
                    }
                    result.append(alternatives.get(i).getAnswerText())
                    .append("</option>");
        }
        Main.logger.info("All answer alternatives returned to client");
        return new HttpResponse(ResponseCode.OK, result.toString(), "text/html");
    }

    public void setQueryParameters(String queryString) {
        queryParameters = HttpRequest.parseQueryParameters(queryString);
    }
}
