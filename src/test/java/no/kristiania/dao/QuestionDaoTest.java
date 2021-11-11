package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.http.HttpClient;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpServer;
import no.kristiania.dao.model.Question;
import no.kristiania.http.controllers.FileController;
import no.kristiania.http.controllers.QuestionsController;
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
                new QuestionsController(dao));
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

    @Test
    void shouldAddAndCheckUTF8Encoding() throws IOException, SQLException {
        String questionText = "Liker du blå gjæss ellær?";
        String questionTitle = "Blå gjæss ellær";
        Question question = new Question(questionTitle, questionText);
        QuestionDao dao = new QuestionDao(TestData.testDataSource());
        dao.save(question, dao.getSaveString());
        server.addController("/api/questions",
                new QuestionsController(dao));
        server.addController("/allQuestions.html",
                new FileController(server));

        HttpClient client = new HttpClient("localhost", server.getPort(),
                "/api/questions", HttpMethod.POST,
                "dbAction=save&questionTitle=" + questionTitle + "&questionText=" + questionText);


        assertThat(dao.retrieveById(question.getId(), dao.getRetrieveByIdString()))
                .isEqualTo(question)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison();
    }
}
