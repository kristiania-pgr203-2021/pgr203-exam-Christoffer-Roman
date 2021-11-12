package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.http.HttpServer;
import org.junit.jupiter.api.Test;

public class AnswerDaoTest {

    // TODO: write tests

    HttpServer server = new HttpServer();
    AnswerDao answerDao = new AnswerDao(TestData.testDataSource());
    QuestionDao questionDao = new QuestionDao(TestData.testDataSource());

    @Test
    void placeholder() {

    }
}
