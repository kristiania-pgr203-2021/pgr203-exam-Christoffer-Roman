package no.kristiania.dao;

import no.kristiania.dao.model.Answer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class AnswerDao extends AbstractDao<Answer> {

    private final String saveString = "insert into answers (answer_text, question_id) values (?, ?)";
    private final String retrieveByIdString = "select * from answers where id = ?";
    private final String retrieveAllString = "select * from answers";
    private final String updateString = "update answers set answer_text = ? where id = ?";
    private final String retrieveByQuestionIdString = "select * from answers where question_id = ?";

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
        // NEVER USED
        return retrieveAllString;
    }

    @Override
    public String getUpdateString() {
        // NEVER USED
        return updateString;
    }

    @Override
    public void setColumnsForSave(Answer answer, PreparedStatement statement) throws SQLException {
        statement.setString(1, answer.getAnswerText());
        statement.setLong(2, answer.getQuestionId());
    }

    @Override
    public void setColumnsForUpdate(Answer answer, PreparedStatement statement) throws SQLException {
        // NEVER USED
        statement.setString(1, answer.getAnswerText());
        statement.setString(2, Long.toString(answer.getId()));
    }

    @Override
    protected Answer mapFromResultSet(ResultSet rs) throws SQLException {
        Answer a = new Answer(rs.getString("answer_text"), rs.getLong("question_id"));
        a.setId(rs.getLong("id"));
        return a;
    }

    public List<Answer> retrieveByQuestionId(long questionId, String retrieveByQuestionIdString) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    retrieveByQuestionIdString, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, questionId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return mapAllFromResultSet(resultSet);
                }
            }
        }
    }

}
