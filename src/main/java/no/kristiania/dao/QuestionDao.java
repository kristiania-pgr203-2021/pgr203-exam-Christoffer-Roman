package no.kristiania.dao;

import javax.sql.DataSource;

public class QuestionDao {
    private DataSource dataSource;

    public QuestionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
