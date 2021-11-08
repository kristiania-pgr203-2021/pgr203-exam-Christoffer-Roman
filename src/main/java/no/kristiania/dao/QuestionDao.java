package no.kristiania.dao;

import no.kristiania.model.Question;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionDao extends Dao {
    private DataSource dataSource;

    private String saveStatement = "insert into questions (question) values (?)";
    private String retrieveStatement = "select * from questions where id = ?";
    private String retrieveAllStatement = "select * from questions";

    public QuestionDao(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    protected void setColumnsForSave(DbObject dbObject, PreparedStatement statement) throws SQLException {
        Question questionObject = (Question) dbObject;
        statement.setString(1, questionObject.getQuestion());
    }

    @Override
    protected DbObject mapFromResultSet(ResultSet rs) throws SQLException {
        return new Question(rs.getLong("id"), rs.getString("question"));
    }

    public String getRetrieveAllStatement() {
        return retrieveAllStatement;
    }

    public String getSaveStatementString() {
        return saveStatement;
    }

    public String getRetrieveStatement() {
        return retrieveStatement;
    }
}