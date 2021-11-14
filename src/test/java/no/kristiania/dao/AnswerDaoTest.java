package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.dao.model.Answer;
import no.kristiania.dao.model.Question;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnswerDaoTest {

    @Test
    void shouldAddAndRetrieveAnswer() throws SQLException {

        QuestionDao questionDao = new QuestionDao(TestData.testDataSource());
        AnswerDao answerDao = new AnswerDao(TestData.testDataSource());

        Question question = new Question("Egg", "Spiser du egg?", Question.QuestionType.REGULAR);
        questionDao.save(question, questionDao.getSaveString());

        Answer answer = new Answer("JA!", question.getId());
        answerDao.save(answer, answerDao.getSaveString());

        assertThat(answerDao.retrieveById(answer.getQuestionId(), answerDao.getRetrieveByQuestionIdString()))
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

        ArrayList<Answer> savedAnswers = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Answer a = new Answer(Integer.toString(i), question.getId());
            answerDao.save(a, answerDao.getSaveString());
            savedAnswers.add(a);
        }

        ArrayList<Answer> retrievedAnswers = (ArrayList<Answer>) answerDao.retrieveByQuestionId(question.getId(), answerDao.getRetrieveByQuestionIdString());

        for (int i = 0; i < 10; i++) {
            assertThat(retrievedAnswers.get(i))
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(savedAnswers.get(i));
        }

        TestData.cleanDataSource(questionDao.getDataSource());
    }
}
