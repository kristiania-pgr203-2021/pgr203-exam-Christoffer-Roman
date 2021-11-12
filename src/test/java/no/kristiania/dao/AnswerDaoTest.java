package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.dao.model.Answer;
import no.kristiania.dao.model.Question;
import no.kristiania.http.HttpServer;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

public class AnswerDaoTest {

    // TODO: write tests

    @Test // Working without questionType
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


}
