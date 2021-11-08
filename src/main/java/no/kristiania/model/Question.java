package no.kristiania.model;

import no.kristiania.dao.DbObject;

public class Question extends DbObject {
    private long id;
    private final String question;

    public Question(long id, String value) {
        this.id = id;
        question = value;
    }

    public Question(String value) {
        question = value;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getQuestion() {
        return question;
    }
}
