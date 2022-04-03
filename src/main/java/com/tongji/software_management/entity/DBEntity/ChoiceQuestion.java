package com.tongji.software_management.entity.DBEntity;

import javax.persistence.*;

@Entity
@Table(name = "choicequestion", schema = "education")
public class ChoiceQuestion {
    private int choiceId;
    private String choiceQuestion;
    private String choiceOption;
    private int choiceDifficulty;
    private String choiceAnswer;
    private String choiceAnalysis;
    private double choiceScore;

    @Id
    @Column(name = "choiceId")
    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    @Basic
    @Column(name = "choiceQuestion")
    public String getChoiceQuestion() {
        return choiceQuestion;
    }

    public void setChoiceQuestion(String choiceQuestion) {
        this.choiceQuestion = choiceQuestion;
    }

    @Basic
    @Column(name = "choiceOption")
    public String getChoiceOption() {
        return choiceOption;
    }

    public void setChoiceOption(String choiceOption) {
        this.choiceOption = choiceOption;
    }

    @Basic
    @Column(name = "choiceDifficulty")
    public int getChoiceDifficulty() {
        return choiceDifficulty;
    }

    public void setChoiceDifficulty(int choiceDifficulty) {
        this.choiceDifficulty = choiceDifficulty;
    }

    @Basic
    @Column(name = "choiceAnswer")
    public String getChoiceAnswer() {
        return choiceAnswer;
    }

    public void setChoiceAnswer(String choiceAnswer) {
        this.choiceAnswer = choiceAnswer;
    }

    @Basic
    @Column(name = "choiceAnalysis")
    public String getChoiceAnalysis() {
        return choiceAnalysis;
    }

    public void setChoiceAnalysis(String choiceAnalysis) {
        this.choiceAnalysis = choiceAnalysis;
    }

    @Basic
    @Column(name = "choiceScore")
    public double getChoiceScore() {
        return choiceScore;
    }

    public void setChoiceScore(double choiceScore) {
        this.choiceScore = choiceScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChoiceQuestion that = (ChoiceQuestion) o;

        if (choiceId != that.choiceId) return false;
        if (choiceDifficulty != that.choiceDifficulty) return false;
        if (Double.compare(that.choiceScore, choiceScore) != 0) return false;
        if (choiceQuestion != null ? !choiceQuestion.equals(that.choiceQuestion) : that.choiceQuestion != null)
            return false;
        if (choiceOption != null ? !choiceOption.equals(that.choiceOption) : that.choiceOption != null) return false;
        if (choiceAnswer != null ? !choiceAnswer.equals(that.choiceAnswer) : that.choiceAnswer != null) return false;
        if (choiceAnalysis != null ? !choiceAnalysis.equals(that.choiceAnalysis) : that.choiceAnalysis != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = choiceId;
        result = 31 * result + (choiceQuestion != null ? choiceQuestion.hashCode() : 0);
        result = 31 * result + (choiceOption != null ? choiceOption.hashCode() : 0);
        result = 31 * result + choiceDifficulty;
        result = 31 * result + (choiceAnswer != null ? choiceAnswer.hashCode() : 0);
        result = 31 * result + (choiceAnalysis != null ? choiceAnalysis.hashCode() : 0);
        temp = Double.doubleToLongBits(choiceScore);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
