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
@Table(name = "practice_score", schema = "education", catalog = "")
@IdClass(PracticeScorePK.class)
public class PracticeScore {
    private String courseId;
    private String classId;
    private String practiceName;
    private String studentNumber;
    private double individualScore;
    private Timestamp individualTime;
    private int groupNumber;

    @Id
    @Column(name = "course_id")
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Id
    @Column(name = "class_id")
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Id
    @Column(name = "practice_name")
    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    @Id
    @Column(name = "student_number")
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    @Basic
    @Column(name = "individual_score")
    public double getIndividualScore() {
        return individualScore;
    }

    public void setIndividualScore(double individualScore) {
        this.individualScore = individualScore;
    }

    @Basic
    @Column(name = "individual_time")
    public Timestamp getIndividualTime() {
        return individualTime;
    }

    public void setIndividualTime(Timestamp individualTime) {
        this.individualTime = individualTime;
    }

    @Basic
    @Column(name = "group_number")
    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PracticeScore that = (PracticeScore) o;
        return Double.compare(that.individualScore, individualScore) == 0 && groupNumber == that.groupNumber && Objects.equals(courseId, that.courseId) && Objects.equals(classId, that.classId) && Objects.equals(practiceName, that.practiceName) && Objects.equals(studentNumber, that.studentNumber) && Objects.equals(individualTime, that.individualTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, classId, practiceName, studentNumber, individualScore, individualTime, groupNumber);
    }
}
