package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.http.HttpServer;
import no.kristiania.model.Question;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class QuestionDaoTest {
    HttpServer server = new HttpServer();

    @Test
    void shouldAddAndReturnQuestion() throws SQLException {
        Question question = new Question("Kan du backflip?");
        QuestionDao dao = new QuestionDao(TestData.testDataSource());
        dao.save(question, dao.getSaveString());

        assertThat(dao.retrieveById(question.getId(), dao.getRetrieveByIdString()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(question);
    }
}
