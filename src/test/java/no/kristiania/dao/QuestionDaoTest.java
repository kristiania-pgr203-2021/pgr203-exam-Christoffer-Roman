package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.dao.model.Question;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionDaoTest {

    @Test
    void shouldAddAndReturnRegularQuestion() throws SQLException {

        QuestionDao dao = new QuestionDao(TestData.testDataSource());

        Question question = new Question("Backflip", "Kan du backflip?", Question.QuestionType.REGULAR);
        dao.save(question, dao.getSaveString());

        assertThat(dao.retrieveById(question.getId(), dao.getRetrieveByQuestionIdString()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(question);

        TestData.cleanDataSource(dao.getDataSource());
    }

    @Test
    void shouldRetrieveAllQuestions() throws SQLException {

        QuestionDao dao = new QuestionDao(TestData.testDataSource());

        ArrayList<Question> questions = new ArrayList<>();

        Question qReg = new Question("RegTest", "Er dette en tekst?", Question.QuestionType.REGULAR);
        dao.save(qReg, dao.getSaveString());
        questions.add(qReg);

        Question qSca = new Question("ScaTest", "Er dette en skala?", Question.QuestionType.SCALE);
        dao.save(qSca, dao.getSaveString());
        questions.add(qSca);

        Question qMul = new Question("MulTest", "Er dette svaralternativer?", Question.QuestionType.MULTIPLE_ANSWERS);
        dao.save(qMul, dao.getSaveString());
        questions.add(qMul);

        for (Question q : questions) {
            assertThat(dao.retrieveById(q.getId(), dao.getRetrieveByQuestionIdString()))
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(q);
        }

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

        assertThat(dao.retrieveById(updatedQuestion.getId(), dao.getRetrieveByQuestionIdString()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(updatedQuestion);

        TestData.cleanDataSource(dao.getDataSource());
    }
}
