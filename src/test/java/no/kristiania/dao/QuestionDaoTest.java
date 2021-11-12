package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.dao.model.Question;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public class QuestionDaoTest {

    @Test
    void shouldAddAndReturnQuestion() throws SQLException {

        QuestionDao dao = new QuestionDao(TestData.testDataSource());

        Question question = new Question("Backflip", "Kan du backflip?", Question.QuestionType.REGULAR);
        dao.save(question, dao.getSaveString());

        assertThat(dao.retrieveById(question.getId(), dao.getRetrieveByIdString()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(question);

        TestData.cleanDataSource(dao.getDataSource());
    }

    @Test
    void shouldUpdateQuestion() throws SQLException {

        QuestionDao dao = new QuestionDao(TestData.testDataSource());

        Question savedQuestion = new Question("Foreldre", "Who's your daddy?", Question.QuestionType.REGULAR);
        dao.save(savedQuestion, dao.getSaveString());

        Question updatedQuestion = new Question("Foreldre", "Who's your mama?", Question.QuestionType.REGULAR);
        updatedQuestion.setId(savedQuestion.getId());
        dao.update(updatedQuestion, dao.getUpdateString());

        // TODO: Does not take question type into account! QuestionDao.updateString must be changed!
        assertThat(dao.retrieveById(updatedQuestion.getId(), dao.getRetrieveByIdString()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(updatedQuestion);

        TestData.cleanDataSource(dao.getDataSource());
    }
}
