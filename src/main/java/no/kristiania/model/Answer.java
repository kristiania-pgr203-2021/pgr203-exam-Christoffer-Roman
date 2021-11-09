package no.kristiania.model;

public class Answer extends AbstractModel{

    private final String answerText;

    public Answer(String answer) {
        this.answerText = answer;
    }

    public String getAnswerText(){
        return answerText;
    }
}
