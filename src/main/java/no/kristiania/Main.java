package no.kristiania;

import no.kristiania.dao.AnswerAlternativeDao;
import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.http.HttpServer;
import no.kristiania.http.controllers.AnswerAlternativesController;
import no.kristiania.http.controllers.AnswersController;
import no.kristiania.http.controllers.FileController;
import no.kristiania.http.controllers.QuestionsController;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private static DataSource dataSource;
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(8008);
        dataSource = createDataSource();
        QuestionDao questionDao = new QuestionDao(dataSource);
        AnswerDao answerDao = new AnswerDao(dataSource);
        AnswerAlternativeDao alternativeDao = new AnswerAlternativeDao(dataSource);
        server.addController(QuestionsController.PATH, new QuestionsController(questionDao));
        server.addController(AnswersController.PATH, new AnswersController(answerDao));
        server.addController(AnswerAlternativesController.PATH, new AnswerAlternativesController(alternativeDao));
        FileController fileController = new FileController();
        for (String path : FileController.PATHS()) {
            server.addController(path, fileController);
        }
        logger.info("Server running on: http://localhost:"+ server.getPort());
    }

    private static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url",
                "jdbc:postgresql://localhost:5432/questionnaire"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword("dataSource.password");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
