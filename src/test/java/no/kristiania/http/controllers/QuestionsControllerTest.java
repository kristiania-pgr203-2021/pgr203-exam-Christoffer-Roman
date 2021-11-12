package no.kristiania.http.controllers;

import no.kristiania.TestData;
import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.model.Question;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpServer;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionsControllerTest {

    HttpServer server = new HttpServer();

    @Test
    void shouldAddAndReturnQuestionViaPostRequest() throws SQLException {

        QuestionDao dao = new QuestionDao(TestData.testDataSource());

        server.addController("/api/questions", new QuestionsController(dao));

        HttpRequest request = new HttpRequest(HttpMethod.POST, "/api/questions");
        request.setQueryString("dbAction=save&questionType=regular&questionTitle=Cola&questionText=Liker du cola?");

        QuestionsController qCon = (QuestionsController)server.getController("/api/questions");
        qCon.setQueryParameters(request.getQueryString());
        qCon.handle(request);

        Question expectedQuestion = new Question("Cola", "Liker du cola?", Question.QuestionType.REGULAR);
        expectedQuestion.setId(1);

        Question retrievedQuestion = dao.retrieveById(expectedQuestion.getId(), dao.getRetrieveByIdString());

        assertThat(retrievedQuestion)
                .isEqualTo(expectedQuestion)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison();

        TestData.cleanDataSource(dao.getDataSource());
    }

    @Test
    void shouldUpdateQuestionViaPostRequest() throws SQLException {

        QuestionDao dao = new QuestionDao(TestData.testDataSource());

        Question savedQuestion = new Question("Sult", "Er du mett?", Question.QuestionType.REGULAR);
        dao.save(savedQuestion, dao.getSaveString());

        server.addController("/api/questions", new QuestionsController(dao));

        HttpRequest request = new HttpRequest(HttpMethod.POST, "/api/questions");
        request.setQueryString(
                "dbAction=update&id="
                        + savedQuestion.getId()
                        + "&questionType=regular&questionTitle=Sult&questionText=Er du sulten?");

        QuestionsController qCon = (QuestionsController)server.getController("/api/questions");
        qCon.setQueryParameters(request.getQueryString());
        qCon.handle(request);

        Question expectedQuestion = new Question("Sult", "Er du sulten?", Question.QuestionType.REGULAR);
        expectedQuestion.setId(savedQuestion.getId());

        Question retrievedQuestion = dao.retrieveById(expectedQuestion.getId(), dao.getRetrieveByIdString());

        // TODO: Does not take question type into account! QuestionDao.updateString must be changed and QuestionsController refactored!
        assertThat(retrievedQuestion)
                .isEqualTo(expectedQuestion)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison();

        TestData.cleanDataSource(dao.getDataSource());
    }
}
