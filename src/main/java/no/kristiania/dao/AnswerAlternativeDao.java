package no.kristiania.dao;

import no.kristiania.dao.model.AnswerAlternative;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerAlternativeDao extends AbstractDao<AnswerAlternative> {

    // TODO: possibly remove class

    public final String saveString = "insert into answer_alternatives(answer, question_id) values (?, ?)";
    public final String retrieveByQuestionIdString = "select * from answer_alternatives where question_id = ?";
    public final String retrieveByIdString = "select * from answer_alternatives where id = ?";
    public final String retrieveAllString = "";
    public final String updateString = "update answer_alternatives set answer = ? where id = ?";

    public AnswerAlternativeDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public String getSaveString() {
        return saveString;
    }

    @Override
    public String getRetrieveByQuestionIdString() {
        return retrieveByQuestionIdString;
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
    public void setColumnsForSave(AnswerAlternative answerAlternative, PreparedStatement statement) throws SQLException {
        statement.setString(1, answerAlternative.getAnswerText());
        statement.setLong(2, answerAlternative.getQuestionId());
    }

    @Override
    public void setColumnsForUpdate(AnswerAlternative alternative, PreparedStatement statement) throws SQLException {
        statement.setString(1, alternative.getAnswerText());
        statement.setLong(2, alternative.getId());
    }

    @Override
    public AnswerAlternative mapFromResultSet(ResultSet resultSet) throws SQLException {
        AnswerAlternative alternative = new AnswerAlternative(
                resultSet.getString("answer"),
                resultSet.getLong("question_id"));
        alternative.setId(resultSet.getLong("id"));

        return alternative;
    }
}
