package no.kristiania.dao;

import no.kristiania.model.Answer;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerDao extends AbstractDao<Answer> {

    private final String saveString = "insert into answers (answerText) values (?)";
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
    public void setColumnsForSave(Answer model, PreparedStatement statement) throws SQLException {
        statement.setString(1, model.getAnswerText());
    }

    @Override
    protected Answer mapFromResultSet(ResultSet rs) throws SQLException {
        Answer a = new Answer(rs.getString("answerText"));
        a.setId(rs.getLong("id"));
        return a;
    }

}
