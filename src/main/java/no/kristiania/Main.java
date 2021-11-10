package no.kristiania;

import no.kristiania.dao.QuestionDao;
import no.kristiania.http.HttpServer;
import no.kristiania.http.controllers.FileController;
import no.kristiania.http.controllers.ManyQuestionsController;
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

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(8008);
        DataSource dataSource = createDataSource();
        QuestionDao dao = new QuestionDao(dataSource);
        FileController fileController = new FileController(server);
        server.addController("/index.html", fileController);
        server.addController("/allAnswers.html", fileController);
        server.addController("/addAnswer.html", fileController);
        server.addController("/editQuestion.html", fileController);
        server.addController("/addQuestion.html", fileController);
        server.addController("/allQuestions.html", fileController);
        server.addController("/style.css", fileController);
        server.addController("/api/questions", new ManyQuestionsController(dao));
        logger.info("Server running on: http://localhost:{}", server.getPort());
    }

    private static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url",
                "jdbc:postgresql://localhost:5432/questionnaire"));
        dataSource.setUser(properties.getProperty("dataSource.user"));
        dataSource.setPassword("dataSource.password");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
