package no.kristiania.dao;

import no.kristiania.dao.model.AbstractModel;

import javax.sql.DataSource;
import java.sql.*;

public abstract class AbstractDao {

    protected final DataSource dataSource;

    protected AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource(){
        return dataSource;
    }

    public void save(AbstractModel model, String statementString) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    statementString,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                setColumnsForSave(model, statement);
                statement.executeUpdate();
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    model.setId(rs.getLong("id"));
                }
            }
        }
    }

    protected abstract void setColumnsForSave(AbstractModel model, PreparedStatement statement) throws SQLException;
}

