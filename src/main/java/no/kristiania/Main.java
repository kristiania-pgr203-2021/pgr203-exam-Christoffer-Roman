package no.kristiania;

import no.kristiania.dao.QuestionDao;
import no.kristiania.http.HttpServer;
import no.kristiania.http.controllers.FileController;
import no.kristiania.http.controllers.QuestionsController;
import org.flywaydb.core.Flyway;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
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
        server.addController("/index.html", new FileController(server));
        server.addController("/allQuestions.html", new FileController(server));
        server.addController("/api/questions", new QuestionsController(dao));
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
