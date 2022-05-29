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
@Table(name = "exp_score", schema = "education", catalog = "")
@IdClass(ExpScorePK.class)
public class ExpScore {
    private String studentNumber;
    private String courseId;
    private String expname;
    private String classId;
    private double score;
    private String comment;
    private String fileUrl;

    @Id
    @Column(name = "student_number")
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

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

    @Id
    @Column(name = "class_id")
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Basic
    @Column(name = "score")
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Basic
    @Column(name = "file_url")
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpScore expScore = (ExpScore) o;
        return Double.compare(expScore.score, score) == 0 && Objects.equals(studentNumber, expScore.studentNumber) && Objects.equals(courseId, expScore.courseId) && Objects.equals(expname, expScore.expname) && Objects.equals(classId, expScore.classId) && Objects.equals(comment, expScore.comment) && Objects.equals(fileUrl, expScore.fileUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber, courseId, expname, classId, score, comment, fileUrl);
    }
}
