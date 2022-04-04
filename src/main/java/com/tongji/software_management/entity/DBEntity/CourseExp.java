package com.tongji.software_management.entity.DBEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courseexp", schema = "education")
@IdClass(CourseExpEntityPK.class)
public class CourseExp {
    private String courseId;
    private String expname;
    private int percent;
    private int priority;
    private int difficulty;


    @Id
    @Column(name = "courseID")
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

        CourseExp that = (CourseExp) o;

        if (percent != that.percent) return false;
        if (priority != that.priority) return false;
        if (difficulty != that.difficulty) return false;
        if (courseId != null ? !courseId.equals(that.courseId) : that.courseId != null) return false;
        if (expname != null ? !expname.equals(that.expname) : that.expname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = courseId != null ? courseId.hashCode() : 0;
        result = 31 * result + (expname != null ? expname.hashCode() : 0);
        result = 31 * result + percent;
        result = 31 * result + priority;
        result = 31 * result + difficulty;
        return result;
    }
}
