package no.kristiania.dao;

import no.kristiania.model.Question;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionDao extends AbstractDao<Question> {

    public QuestionDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public String getSaveString() {
        return null;
    }

    @Override
    public String getRetrieveByIdString() {
        return null;
    }

    @Override
    public String getRetrieveAllString() {
        return null;
    }

    @Override
    public void setColumnsForSave(Question model, PreparedStatement statement) throws SQLException {

    }

    @Override
    public Question mapFromResultSet(ResultSet resultSet) throws SQLException {
        // TODO: Implement method
        return null;
    }

}
