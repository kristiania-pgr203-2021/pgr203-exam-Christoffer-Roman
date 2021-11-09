package no.kristiania.dao;

import no.kristiania.model.Question;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionDao extends AbstractDao<Question> {

    private final String saveString = "insert into questions (questionText) values (?)";
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
    public void setColumnsForSave(Question model, PreparedStatement statement) throws SQLException {
        statement.setString(1, model.getQuestionText());
    }

    @Override
    public Question mapFromResultSet(ResultSet rs) throws SQLException {
        Question q = new Question(rs.getString("questionText"));
        q.setId(rs.getLong("id"));
        return q;
    }

}
