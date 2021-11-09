package no.kristiania.model;

public class Question extends AbstractModel {

    private final String questionText;
    private final long questionnaireId;

    public Question(String questionText, long questionnaireId) {
        this.questionText = questionText;
        this.questionnaireId = questionnaireId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public long getQuestionnaireId(){
        return questionnaireId;
    }
}
