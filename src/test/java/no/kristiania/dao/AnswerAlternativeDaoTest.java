package no.kristiania.dao;

import no.kristiania.TestData;
import no.kristiania.dao.model.AnswerAlternative;
import no.kristiania.dao.model.Question;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnswerAlternativeDaoTest {

    @Test
    void shouldAddAndRetrieveAnswerAlternative() throws SQLException {

        QuestionDao qDao = new QuestionDao(TestData.testDataSource());
        AnswerAlternativeDao aDao = new AnswerAlternativeDao(TestData.testDataSource());

        Question q = new Question("Bokstaver", "Hvilken bokstav liker du best?", Question.QuestionType.MULTIPLE_ANSWERS);
        qDao.save(q, qDao.getSaveString());

        AnswerAlternative sA = new AnswerAlternative("A", q.getId());
        aDao.save(sA, aDao.getSaveString());

        AnswerAlternative rA = aDao.retrieveAllById(q.getId(), aDao.getRetrieveByQuestionIdString()).get(0);

        assertThat(rA)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(sA);

        TestData.cleanDataSource(qDao.getDataSource());
    }

    @Test
    void shouldRetrieveAllAnswerAlternatives() throws SQLException {

        QuestionDao qDao = new QuestionDao(TestData.testDataSource());
        AnswerAlternativeDao aDao = new AnswerAlternativeDao(TestData.testDataSource());

        Question q = new Question("Bokstaver", "Hvilken bokstav liker du best?", Question.QuestionType.MULTIPLE_ANSWERS);
        qDao.save(q, qDao.getSaveString());

        ArrayList<AnswerAlternative> sList = new ArrayList<>();

        AnswerAlternative aA = new AnswerAlternative("A", q.getId());
        aDao.save(aA, aDao.getSaveString());
        sList.add(aA);

        AnswerAlternative aB = new AnswerAlternative("B", q.getId());
        aDao.save(aB, aDao.getSaveString());
        sList.add(aB);

        AnswerAlternative aC = new AnswerAlternative("C", q.getId());
        aDao.save(aC, aDao.getSaveString());
        sList.add(aC);

        ArrayList<AnswerAlternative> rList = (ArrayList<AnswerAlternative>) aDao.retrieveAllById(q.getId(), aDao.getRetrieveByQuestionIdString());

        for (int i = 0; i < sList.size(); i++) {
            assertThat(rList.get(i))
                    .hasNoNullFieldsOrProperties()
                    .usingRecursiveComparison()
                    .isEqualTo(sList.get(i));
        }

        TestData.cleanDataSource(qDao.getDataSource());
    }
}
