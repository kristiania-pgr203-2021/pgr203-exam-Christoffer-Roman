package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.http.HttpServer;
import org.junit.jupiter.api.Test;

public class QuestionDaoTest {
    HttpServer server = new HttpServer();

    @Test
    void shouldAddQuestionAndRetrieve() {
        QuestionDao dao = new QuestionDao(TestData.testDataSource());
        server.addController("/api/questions", new QuestionsController(dao));
    }
}
