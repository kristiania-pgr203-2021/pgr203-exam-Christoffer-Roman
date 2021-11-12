package no.kristiania.dao.model;

public class AnswerAlternative extends AbstractModel {
    // TODO: possibly remove class

    private long id;
    private String questionText;
    private long questionId;

    public AnswerAlternative(String questionText, long questionId) {
        this.questionText = questionText;
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }
}
