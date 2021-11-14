package no.kristiania.dao.model;

public class Answer extends AbstractModel{

    private String answerText;
    private final long questionId;

    public Answer(String answerText, long questionId) {
        this.answerText = answerText;
        this.questionId = questionId;
    }

    public String getAnswerText(){
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public long getQuestionId() {
        return questionId;
    }
}
