package no.kristiania.dao.model;

import java.util.Objects;

public class Question extends AbstractModel {

    private String questionTitle;
    private String questionText;

    public Question(String questionTitle, String questionText) {
        this.questionTitle = questionTitle;
        this.questionText = questionText;
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
