package com.tongji.software_management.entity.DBEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "choice_question", schema = "education", catalog = "")
public class ChoiceQuestion {
    private int choiceId;
    private String choiceAnalysis;
    private String choiceAnswer;
    private Integer choiceDifficulty;
    private String choiceOption;
    private String choiceQuestion;
    private Double choiceScore;

    @Id
    @Column(name = "choice_id")
    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    @Basic
    @Column(name = "choice_analysis")
    public String getChoiceAnalysis() {
        return choiceAnalysis;
    }

    public void setChoiceAnalysis(String choiceAnalysis) {
        this.choiceAnalysis = choiceAnalysis;
    }

    @Basic
    @Column(name = "choice_answer")
    public String getChoiceAnswer() {
        return choiceAnswer;
    }

    public void setChoiceAnswer(String choiceAnswer) {
        this.choiceAnswer = choiceAnswer;
    }

    @Basic
    @Column(name = "choice_difficulty")
    public Integer getChoiceDifficulty() {
        return choiceDifficulty;
    }

    public void setChoiceDifficulty(Integer choiceDifficulty) {
        this.choiceDifficulty = choiceDifficulty;
    }

    @Basic
    @Column(name = "choice_option")
    public String getChoiceOption() {
        return choiceOption;
    }

    public void setChoiceOption(String choiceOption) {
        this.choiceOption = choiceOption;
    }

    @Basic
    @Column(name = "choice_question")
    public String getChoiceQuestion() {
        return choiceQuestion;
    }

    public void setChoiceQuestion(String choiceQuestion) {
        this.choiceQuestion = choiceQuestion;
    }

    @Basic
    @Column(name = "choice_score")
    public Double getChoiceScore() {
        return choiceScore;
    }

    public void setChoiceScore(Double choiceScore) {
        this.choiceScore = choiceScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChoiceQuestion that = (ChoiceQuestion) o;
        return choiceId == that.choiceId && Objects.equals(choiceAnalysis, that.choiceAnalysis) && Objects.equals(choiceAnswer, that.choiceAnswer) && Objects.equals(choiceDifficulty, that.choiceDifficulty) && Objects.equals(choiceOption, that.choiceOption) && Objects.equals(choiceQuestion, that.choiceQuestion) && Objects.equals(choiceScore, that.choiceScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(choiceId, choiceAnalysis, choiceAnswer, choiceDifficulty, choiceOption, choiceQuestion, choiceScore);
    }
}
