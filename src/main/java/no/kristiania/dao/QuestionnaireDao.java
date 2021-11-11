package no.kristiania.dao;

import no.kristiania.dao.model.AnswerAlternative;
import no.kristiania.dao.model.Questionnarie;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionnaireDao extends AbstractDao<Questionnarie> {

    // TODO: possibly remove class

    private final String saveString = "insert into questionnaires (name) values (?)";
    private final String retrieveByIdString = "select * from questionnaires where id = ?";
    private final String retrieveAllString = "select * from questionnaires";

    public QuestionnaireDao(DataSource dataSource) {
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
    public void setColumnsForSave(Questionnarie model, PreparedStatement statement) throws SQLException {
        statement.setString(1, model.getName());
    }

    @Override
    public void setColumnsForUpdate(Questionnarie questionnarie, PreparedStatement statement) throws SQLException {
        // TODO: Implement method
    }

    @Override
    protected Questionnarie mapFromResultSet(ResultSet rs) throws SQLException {
        Questionnarie q = new Questionnarie(rs.getString("name"));
        q.setId(rs.getLong("id"));
        return q;
    }


}
