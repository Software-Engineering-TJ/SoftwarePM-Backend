package com.tongji.software_management.entity.DBEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Practice {
    private String practiceId;
    private String practiceName;
    private String courseId;
    private String classId;
    private Timestamp endTime;
    private Timestamp startTime;
    private String choice_id;

    @Id
    @Column(name = "practice_id")
    public String getPracticeId() {return practiceId;}

    public void setPracticeId(String practiceId) {this.practiceId = practiceId;}

    @Basic
    @Column(name = "practice_name")
    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    @Basic
    @Column(name = "course_id")
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Basic
    @Column(name = "class_id")
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Basic
    @Column(name = "end_time")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "start_time")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "choice_id")
    public String getChoiceId() {return choice_id;}

    public void setChoiceId(String choice_id) {this.choice_id = choice_id;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Practice practice = (Practice) o;
        return Objects.equals(practiceName, practice.practiceName) && Objects.equals(courseId, practice.courseId) && Objects.equals(classId, practice.classId) && Objects.equals(endTime, practice.endTime) && Objects.equals(startTime, practice.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(practiceName, courseId, classId, endTime, startTime);
    }
}
