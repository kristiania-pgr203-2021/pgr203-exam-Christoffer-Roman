package no.kristiania.dao;

import no.kristiania.model.Answer;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerDao extends AbstractDao<Answer> {



    public AnswerDao(DataSource dataSource) {
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
    public void setColumnsForSave(Answer model, PreparedStatement statement) throws SQLException {

    }

    @Override
    protected Answer mapFromResultSet(ResultSet resultSet) throws SQLException {
        return null;
    }

}
