package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.dao.model.Answer;
import no.kristiania.dao.model.Question;
import no.kristiania.http.HttpServer;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;

// Only working without questionType
public class AnswerDaoTest {

    // TODO: write tests

    @Test
    void shouldAddAndRetrieveQuestion() throws SQLException {

        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        AnswerDao answerDao = new AnswerDao(TestData.testDataSource());

        Question question = new Question("Egg", "Spiser du egg?", Question.QuestionType.REGULAR);
        questionDao.save(question, questionDao.getSaveString());

        Answer answer = new Answer("JA!", question.getId());
        answerDao.save(answer, answerDao.getSaveString());

        assertThat(answerDao.retrieveById(answer.getQuestionId(), answerDao.getRetrieveByIdString()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(answer);

        TestData.cleanDataSource(questionDao.getDataSource());
    }

    @Test
    void shouldRetrieveAllAnswersToQuestion() throws SQLException {

        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        AnswerDao answerDao = new AnswerDao(TestData.testDataSource());

        Question question = new Question("Kjæledyr", "Hvor mange kjæledyr har du hatt?", Question.QuestionType.REGULAR);
        questionDao.save(question, questionDao.getSaveString());

        ArrayList<Answer> answers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Answer a = new Answer(Integer.toString(i), question.getId());
            answerDao.save(a, answerDao.getSaveString());
            answers.add(a);
        }

        for (Answer a : answers) {
            assertThat(answerDao.retrieveById(question.getId(), answerDao.getRetrieveByIdString()))
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(a);
        }

        TestData.cleanDataSource(questionDao.getDataSource());
    }
}
