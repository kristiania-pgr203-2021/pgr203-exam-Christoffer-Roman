package no.kristiania.dao;

import no.kristiania.model.AbstractModel;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionaireDao extends AbstractDao {

    public QuestionaireDao(DataSource dataSource) {
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
    public void setColumnsForSave(AbstractModel model, PreparedStatement statement) throws SQLException {

    }

    @Override
    protected AbstractModel mapFromResultSet(ResultSet resultSet) throws SQLException {
        return null;
    }


}
