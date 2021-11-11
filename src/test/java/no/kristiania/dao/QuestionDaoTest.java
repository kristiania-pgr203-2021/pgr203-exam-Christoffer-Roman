package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.http.HttpClient;
import no.kristiania.http.HttpMethod;
import no.kristiania.http.HttpRequest;
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
    void shouldAddAndReturnQuestionViaPost() throws IOException, SQLException, InterruptedException {
        Question question = new Question("Cola", "Liker du cola?");
        QuestionDao dao = new QuestionDao(TestData.testDataSource());
        //assertEquals(1, question.getId());

        server.addController("/api/questions",
                new QuestionsController(dao));
        server.addController("/allQuestions.html",
                new FileController(server));

        /*
        HttpClient client = new HttpClient("localhost", server.getPort(),
                "/api/questions", HttpMethod.GET,
                "id=1&questionTitle=Cola&questionText=Liker du cola?");
        */

        HttpRequest request = new HttpRequest(HttpMethod.POST, "/api/questions");
        request.setQueryString("questionTitle=Cola&questionText=Liker du cola?");

        server.getController("/api/questions").handle(request);

        long id = question.getId();

        Question retrievedQuestion = dao.retrieveById(1, dao.getRetrieveByIdString());
        //assertEquals(1, questions.size());

        assertThat(retrievedQuestion)
                .isEqualTo(question)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison();

    }

    @Test
    void shouldAddAndCheckUTF8Encoding() throws IOException, SQLException, InterruptedException {
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
