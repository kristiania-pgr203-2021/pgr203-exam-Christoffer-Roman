package no.kristiania.dao;

import no.kristiania.model.Question;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionDao extends AbstractDao<Question> {

    private final String saveString = "insert into questions (question_text, questionnaire_id) values (?, ?)";
    private final String retrieveByIdString = "select * from questions where id = ?";
    private final String retrieveAllString = "select * from questions";

    public QuestionDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public String getSaveString() {
        return saveString;
    }

    @Override
    public String getRetrieveByIdString() {
        return retrieveByIdString;
    }

    @Override
    public String getRetrieveAllString() {
        return retrieveAllString;
    }

    @Override
    public void setColumnsForSave(Question question, PreparedStatement statement) throws SQLException {
        statement.setString(1, question.getQuestionText());
        statement.setString(2, Long.toString(question.getQuestionnaireId()));
    }

    @Override
    public Question mapFromResultSet(ResultSet rs) throws SQLException {
        Question q = new Question(rs.getString("question_text"), rs.getLong("questionnaire_id"));
        q.setId(rs.getLong("id"));
        return q;
    }

}
