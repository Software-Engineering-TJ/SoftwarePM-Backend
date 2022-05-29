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
@Table(name = "course_exp", schema = "education", catalog = "")
@IdClass(CourseExpPK.class)
public class CourseExp {
    private String courseId;
    private String expname;
    private int percent;
    private int priority;
    private int difficulty;

    @Id
    @Column(name = "course_id")
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Id
    @Column(name = "expname")
    public String getExpname() {
        return expname;
    }

    public void setExpname(String expname) {
        this.expname = expname;
    }

    @Basic
    @Column(name = "percent")
    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    @Basic
    @Column(name = "priority")
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "difficulty")
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseExp courseExp = (CourseExp) o;
        return percent == courseExp.percent && priority == courseExp.priority && difficulty == courseExp.difficulty && Objects.equals(courseId, courseExp.courseId) && Objects.equals(expname, courseExp.expname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, expname, percent, priority, difficulty);
    }
}
