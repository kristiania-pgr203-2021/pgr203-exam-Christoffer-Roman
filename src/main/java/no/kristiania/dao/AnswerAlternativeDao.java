package no.kristiania.dao;

import no.kristiania.dao.model.AnswerAlternative;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerAlternativeDao extends AbstractDao<AnswerAlternative> {

    // TODO: possibly remove class

    public final String saveString = "";
    public final String retrieveByIdString = "";
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
        // TODO: Implement method
    }

    @Override
    public AnswerAlternative mapFromResultSet(ResultSet resultSet) throws SQLException {
        // TODO: Implement method
        return null;
    }
}
