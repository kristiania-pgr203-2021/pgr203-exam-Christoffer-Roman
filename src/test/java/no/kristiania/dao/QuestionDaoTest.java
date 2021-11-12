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
    QuestionDao dao = new QuestionDao(TestData.testDataSource());

    @Test
    void shouldAddAndReturnQuestion() throws SQLException {

        Question question = new Question("Backflip", "Kan du backflip?", Question.QuestionType.REGULAR);
        dao.save(question, dao.getSaveString());

        assertThat(dao.retrieveById(question.getId(), dao.getRetrieveByIdString()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(question);
    }

    @Test
    void shouldUpdateQuestion() throws SQLException {

        Question savedQuestion = new Question("Foreldre", "Who's your daddy?", Question.QuestionType.REGULAR);
        dao.save(savedQuestion, dao.getSaveString());

        Question updatedQuestion = new Question("Foreldre", "Who's your mama?", Question.QuestionType.REGULAR);
        updatedQuestion.setId(savedQuestion.getId());
        dao.update(updatedQuestion, dao.getUpdateString());

        // TODO: Does not take question type into account! QuestionDao.updateString must be changed
        assertThat(dao.retrieveById(updatedQuestion.getId(), dao.getRetrieveByIdString()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(updatedQuestion);
    }

    @Test
    @Order(1) // Test will fail if not first
    void shouldAddAndReturnQuestionViaPostRequest() throws SQLException {

        server.addController("/api/questions", new QuestionsController(dao));

        HttpRequest request = new HttpRequest(HttpMethod.POST, "/api/questions");
        request.setQueryString("dbAction=save&questionType=regular&questionTitle=Cola&questionText=Liker du cola?");

        QuestionsController qCon = (QuestionsController)server.getController("/api/questions");
        qCon.setQueryParameters(request.getQueryString());
        qCon.handle(request);

        Question question = new Question("Cola", "Liker du cola?", Question.QuestionType.REGULAR);
        // Because QuestionsController never returns the Question-object it creates,
        // the test has to use a static ID value for assertions. Order of tests is
        // therefore critical
        question.setId(1);

        Question retrievedQuestion = dao.retrieveById(question.getId(), dao.getRetrieveByIdString());

        assertThat(retrievedQuestion)
                .isEqualTo(question)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison();
    }
}
