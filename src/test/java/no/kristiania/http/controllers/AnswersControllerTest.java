package no.kristiania.http.controllers;

import no.kristiania.TestData;
import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.model.Answer;
import no.kristiania.dao.model.Question;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpServer;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class AnswersControllerTest {

    HttpServer server = new HttpServer();

    @Test
    void shouldAddAnswerViaPostRequest() throws SQLException {

        QuestionDao qDao = new QuestionDao(TestData.testDataSource());
        Question q = new Question("Test", "Er dette en test?", Question.QuestionType.REGULAR);
        qDao.save(q, qDao.getSaveString());

        AnswerDao aDao = new AnswerDao(TestData.testDataSource());
        server.addController("/api/answers", new AnswersController(aDao));

        HttpRequest request = new HttpRequest(HttpMethod.POST, "/api/answers");
        request.setQueryString("answerText=Ja&questionId=" + q.getId());

        AnswersController aCon = (AnswersController)server.getController("/api/answers");
        aCon.setQueryParameters(request.getQueryString());
        aCon.handle(request);

        Answer expectedAnswer = new Answer("Ja", q.getId());
        expectedAnswer.setId(1);

        Answer retrievedAnswer = aDao.retrieveByQuestionId(q.getId(), aDao.getRetrieveByQuestionIdString()).get(0);

        assertThat(retrievedAnswer)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(expectedAnswer);

        TestData.cleanDataSource(qDao.getDataSource());
    }
}
