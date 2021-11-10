package no.kristiania.dao.model;

public class Answer extends AbstractModel{

    private final String answerText;
    private final long questionId;

    public Answer(String answerText, long questionId) {
        this.answerText = answerText;
        this.questionId = questionId;
    }

    public String getAnswerText(){
        return answerText;
    }

    public long getQuestionId() {
        return questionId;
    }
}
