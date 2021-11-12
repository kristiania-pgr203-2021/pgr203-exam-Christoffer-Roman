package no.kristiania.dao;

import no.kristiania.dao.model.AbstractModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T extends AbstractModel> {

    protected final DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public abstract String getSaveString();
    public abstract String getRetrieveByIdString();
    public abstract String getRetrieveAllString();
    public abstract String getUpdateString();

    public void save(T model, String statementString) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    statementString,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                setColumnsForSave(model, statement);
                statement.executeUpdate();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    resultSet.next();
                    model.setId(resultSet.getLong("id"));
                }
            }
        }
    }

    public abstract void setColumnsForSave(T model, PreparedStatement statement) throws SQLException;
    public abstract void setColumnsForUpdate(T model, PreparedStatement statement) throws SQLException;

    public T retrieveById(long id, String retrieveStatement) throws SQLException {
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

    public List<T> retrieveAllById(long id, String retrieveStatement) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    retrieveStatement, Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return mapAllFromResultSet(resultSet);
                }
            }
        }
    }

    public void update(T model, String updateString) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    updateString,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                setColumnsForUpdate(model, statement);
                statement.executeUpdate();
            }
        }
    }

    public List<T> retrieveAll(String retrieveAllStatement) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    retrieveAllStatement, Statement.RETURN_GENERATED_KEYS)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    return mapAllFromResultSet(resultSet);
                }
            }
        }
    }

    protected List<T> mapAllFromResultSet(ResultSet rs) throws SQLException {
        ArrayList<T> resultList = new ArrayList<>();
        while (rs.next()) {
            resultList.add(mapFromResultSet(rs));
        }
        return resultList;
    }

    protected abstract T mapFromResultSet(ResultSet resultSet) throws SQLException;
}
