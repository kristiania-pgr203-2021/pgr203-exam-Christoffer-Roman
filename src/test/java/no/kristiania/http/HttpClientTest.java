package no.kristiania.http;

import no.kristiania.HttpClient;
import no.kristiania.TestData;
import no.kristiania.dao.QuestionDao;
import no.kristiania.http.controllers.FileController;
import no.kristiania.http.controllers.QuestionsController;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpClientTest {

    private HttpServer server = new HttpServer();

    @Test
    void shouldRespond200OK() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals(200, client.getResponseCode());
    }

    @Test
    void shouldRespond404NotFound() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80, "/not-there");
        assertEquals(404, client.getResponseCode());
    }

    @Test
    void shouldReadHeaders() throws IOException{
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type"));
    }

    @Test
    void shouldReadMessageBody() throws IOException {
        var client = new HttpClient("httpbin.org", 80, "/html");

        var expected = "<h1>Herman Melville - Moby-Dick</h1>\n";

        assertTrue(client.getMessageBody().contains(expected));
    }

    @Test
    void shouldGetRedirected() throws IOException {
        QuestionDao dao = new QuestionDao(TestData.testDataSource());
        server.addController("/api/questions", new QuestionsController(dao));
        server.addController("/allQuestions.html", new FileController());

        server.setRoot(Path.of("src/main/resources"));
        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/questions", HttpMethod.POST,
                "dbAction=save&questionTitle=Cola&questionText=Liker du cola?");

        assertEquals(200, client.getResponseCode());
        assertEquals("/allQuestions.html", client.getHeader("Target"));
    }
}
