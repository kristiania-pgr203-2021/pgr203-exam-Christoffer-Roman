package no.kristiania.dao.model;

public class AnswerAlternative extends AbstractModel {
    // TODO: possibly remove class

    private String answerText;
    private long questionId;

    public AnswerAlternative(String questionText, long questionId) {
        this.answerText = questionText;
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public long getQuestionId() {
        return questionId;
    }
}
