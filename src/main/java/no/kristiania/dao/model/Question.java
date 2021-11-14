package no.kristiania.dao.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question extends AbstractModel {

    private QuestionType questionType = QuestionType.REGULAR;
    private String questionTitle;
    private String questionText;
    private List<String> alternatives = new ArrayList<>();

    public Question(String questionTitle, String questionText) {
        this.questionTitle = questionTitle;
        this.questionText = questionText;
    }

    public Question(String questionTitle, String questionText, QuestionType questionType) {
        this.questionTitle = questionTitle;
        this.questionText = questionText;
        this.questionType = questionType;
    }


    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuestionType getType() {
        return questionType;
    }

    public void setType(int questionType) {
        this.questionType = QuestionType.values()[questionType];
    }

    public enum QuestionType {
        REGULAR("regular"), MULTIPLE_ANSWERS("multiple_answers"), SCALE("scale");

        private final String type;

        QuestionType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question = (Question) o;
        return questionTitle.equals(question.questionTitle) && Objects.equals(questionText, question.questionText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionTitle, questionText);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionTitle='" + questionTitle + '\'' +
                ", questionText='" + questionText + '\'' +
                '}';
    }
}
