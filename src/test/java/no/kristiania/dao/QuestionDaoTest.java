package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.http.HttpClient;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpServer;
import no.kristiania.dao.model.Question;
import no.kristiania.http.controllers.FileController;
import no.kristiania.http.controllers.ManyQuestionsController;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
    void shouldAddAndReturnQuestionViaPost() throws IOException, SQLException {
        Question question = new Question("Cola", "Liker du cola?");
        QuestionDao dao = new QuestionDao(TestData.testDataSource());

        server.addController("/api/questions",
                new ManyQuestionsController(dao));
        server.addController("/allQuestions.html",
                new FileController(server));

        HttpClient client = new HttpClient("localhost", server.getPort(),
                "/api/questions", HttpMethod.POST,
                "dbAction=save&questionTitle=Cola&questionText=Liker du cola?");

        List<Question> questions = dao.retrieveAll(dao.getRetrieveAllString());
        assertEquals(1, questions.size());

        assertThat(questions.get(0))
                .isEqualTo(question)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison();
    }
}
