package no.kristiania.dao.model;

public class Question extends AbstractModel {

    private final String questionTitle;
    private final String questionText;

    public Question(String questionTitle, String questionText) {
        this.questionTitle = questionTitle;
        this.questionText = questionText;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public String getQuestionText() {
        return questionText;
    }
}
