package no.kristiania.dao;

import no.kristiania.dao.model.Question;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionDao extends AbstractDao<Question> {

    private final String saveString = "insert into questions (question_title, question_text, question_type) values (?, ?, ?)";
    private final String retrieveByIdString = "select * from questions where id = ?";
    private final String retrieveAllString = "select * from questions";
    private final String updateString = "update questions set question_title = ?, question_text = ? where id = ?";

    public QuestionDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public String getSaveString() {
        return saveString;
    }

    @Override
    public String getRetrieveByQuestionIdString() {
        return retrieveByIdString;
    }

    public String getRetrieveAllString() {
        return retrieveAllString;
    }

    @Override
    public String getUpdateString(){
        return updateString;
    }

    @Override
    public void setColumnsForSave(Question question, PreparedStatement statement) throws SQLException {
        statement.setString(1, question.getQuestionTitle());
        statement.setString(2, question.getQuestionText());
        statement.setInt(3, question.getType().ordinal());
    }

    @Override
    public void setColumnsForUpdate(Question question, PreparedStatement statement) throws SQLException {
        statement.setString(1, question.getQuestionTitle());
        statement.setString(2, question.getQuestionText());
        statement.setLong(3, question.getId());
    }

    @Override
    public Question mapFromResultSet(ResultSet rs) throws SQLException {
        Question q = new Question(rs.getString("question_title"), rs.getString("question_text"));
        q.setId(rs.getLong("id"));
        q.setType(rs.getInt("question_type"));
        return q;
    }

}
