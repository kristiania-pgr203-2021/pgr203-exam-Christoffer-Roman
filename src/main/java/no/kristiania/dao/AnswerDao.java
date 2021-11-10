package no.kristiania.dao;

import no.kristiania.dao.model.Answer;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerDao extends AbstractDao<Answer> {

    private final String saveString = "insert into answers (answer_text, question_id) values (?, ?)";
    private final String retrieveByIdString = "select * from answers where id = ?";
    private final String retrieveAllString = "select * from answers";

    public AnswerDao(DataSource dataSource) {
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
    public String getUpdateString() {
        return null;
    }

    @Override
    public void setColumnsForSave(Answer answer, PreparedStatement statement) throws SQLException {
        statement.setString(1, answer.getAnswerText());
        statement.setString(2, Long.toString(answer.getQuestionId()));
    }

    @Override
    protected Answer mapFromResultSet(ResultSet rs) throws SQLException {
        Answer a = new Answer(rs.getString("answer_text"), rs.getLong("question_id"));
        a.setId(rs.getLong("id"));
        return a;
    }

}
