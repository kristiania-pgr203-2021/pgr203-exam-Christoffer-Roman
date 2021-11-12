package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.dao.model.Question;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpRequest;
import no.kristiania.http.HttpServer;
import no.kristiania.http.controllers.QuestionsController;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuestionDaoTest {

    HttpServer server = new HttpServer();

    @Test
    void shouldAddAndReturnQuestion() throws SQLException {
        Question question = new Question("Backflip", "Kan du backflip?");
        QuestionDao dao = new QuestionDao(TestData.testDataSource());
        dao.save(question, dao.getSaveString());

        assertThat(dao.retrieveById(question.getId(), dao.getRetrieveByIdString()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(question);
    }

    @Test
    void shouldUpdateQuestion() throws SQLException {

    }

    @Test
    @Order(1) // Test will fail if not first
    void shouldAddAndReturnQuestionViaPostRequest() throws SQLException {

        QuestionDao dao = new QuestionDao(TestData.testDataSource());

        server.addController("/api/questions", new QuestionsController(dao));

        HttpRequest request = new HttpRequest(HttpMethod.POST, "/api/questions");
        request.setQueryString("dbAction=save&questionType=regular&questionTitle=Cola&questionText=Liker du cola?");

        QuestionsController qCon = (QuestionsController) server.getController("/api/questions");
        qCon.setQueryParameters(request.getQueryString());
        qCon.handle(request);

        Question question = new Question("Cola", "Liker du cola?", Question.QuestionType.REGULAR);
        // Because QuestionsController never returns the Question-object it creates,
        // the test has to use a static ID value for assertions
        question.setId(1);

        Question retrievedQuestion = dao.retrieveById(question.getId(), dao.getRetrieveByIdString());

        assertThat(retrievedQuestion)
                .isEqualTo(question)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison();
    }
}
