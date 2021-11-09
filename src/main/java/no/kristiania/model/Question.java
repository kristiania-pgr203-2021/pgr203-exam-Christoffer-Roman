package no.kristiania.model;

public class Question extends AbstractModel {
    private long id;
    private final String questionText;

    // TODO: possibly remove this constructor
    public Question(long id, String value) {
        this.id = id;
        questionText = value;
    }

    public Question(String value) {
        questionText = value;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }
}
