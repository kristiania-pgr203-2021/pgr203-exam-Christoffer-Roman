package no.kristiania.dao;

import no.kristiania.dao.model.AnswerAlternative;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerAlternativeDao extends AbstractDao<AnswerAlternative> {

    // TODO: possibly remove class

    public final String saveString = "insert into answer_alternatives(answer, question_id) values (?, ?)";
    public final String retrieveByIdString = "select * from answer_alternatives where question_id = ?";
    public final String retrieveAllString = "";

    public AnswerAlternativeDao(DataSource dataSource) {
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
    public void setColumnsForSave(AnswerAlternative answerAlternative, PreparedStatement statement) throws SQLException {
        statement.setString(1, answerAlternative.getQuestionText());
        statement.setLong(2, answerAlternative.getQuestionId());
    }

    @Override
    public void setColumnsForUpdate(AnswerAlternative question, PreparedStatement statement) throws SQLException {
        // TODO: Implement method
    }

    @Override
    public AnswerAlternative mapFromResultSet(ResultSet resultSet) throws SQLException {
        // TODO: Implement method
        return null;
    }
}
