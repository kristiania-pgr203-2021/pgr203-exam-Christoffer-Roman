package no.kristiania.http;

import no.kristiania.HttpClient;
import no.kristiania.TestData;
import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.model.Question;
import no.kristiania.http.controllers.FileController;
import no.kristiania.http.controllers.QuestionsController;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpServerTest {

    HttpServer server = new HttpServer();

    @Test
    void shouldReturn404() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/not-there");
        assertEquals(404, client.getResponseCode());
    }

    @Test
    void shouldReturn404WithRequestTarget() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/not-there");
        assertEquals("404 Not Found", client.getMessageBody());
    }

    @Test
    void shouldHandleMultipleRequest() throws IOException {
        String target = "/index.html";
        server.setRoot(Path.of("src/main/resources"));
        server.addController(target, new FileController());
        HttpClient client = new HttpClient("localhost", server.getPort(), target);
        assertEquals(200, client.getResponseCode());

        client = new HttpClient("localhost", server.getPort(), "/notavailable");
        assertEquals(404, client.getResponseCode());
        client = new HttpClient("localhost", server.getPort(), "/");
        assertEquals(200, client.getResponseCode());
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
                new FileController());

        new HttpClient("localhost", server.getPort(),
                "/api/questions", HttpMethod.POST,
                "dbAction=save&questionTitle=" + questionTitle + "&questionText=" + questionText);

        assertThat(dao.retrieveById(question.getId(), dao.getRetrieveByIdString()))
                .isEqualTo(question)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison();
    }
}