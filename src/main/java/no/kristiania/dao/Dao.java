package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Dao {

    protected final DataSource dataSource;

    public Dao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void save(DbObject dbObject, String statementString) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    statementString,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                setColumnsForSave(dbObject, statement);
                statement.executeUpdate();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    dbObject.setId(resultSet.getLong("id"));
                }
            }
        }
    }

    protected abstract void setColumnsForSave(DbObject dbObject, PreparedStatement statement) throws SQLException;

    public DbObject retrieveById(long id, String retrieveStatement) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    retrieveStatement, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return mapFromResultSet(resultSet);
                }
            }
        }
    }

    public List<DbObject> retrieveAll(String retrieveAllStatement) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    retrieveAllStatement, Statement.RETURN_GENERATED_KEYS)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    return mapAllFromResultSet(resultSet);
                }
            }
        }
    }

    protected List<DbObject> mapAllFromResultSet(ResultSet rs) throws SQLException {
        ArrayList<DbObject> resultList = new ArrayList<>();
        while (rs.next()) {
            resultList.add(mapFromResultSet(rs));
        }
        return resultList;
    }

    protected abstract DbObject mapFromResultSet(ResultSet resultSet) throws SQLException;
}
