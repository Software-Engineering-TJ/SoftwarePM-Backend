package com.tongji.software_management.entity.DBEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "practicescore", schema = "education", catalog = "")
@IdClass(PracticescoreEntityPK.class)
public class PracticescoreEntity {
    private String courseId;
    private String classId;
    private String practiceName;
    private String studentNumber;
    private double individualScore;
    private Timestamp individualTime;
    private int groupNumber;

    @Id
    @Column(name = "courseID")
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Id
    @Column(name = "classID")
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Id
    @Column(name = "practiceName")
    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    @Id
    @Column(name = "studentNumber")
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    @Basic
    @Column(name = "individualScore")
    public double getIndividualScore() {
        return individualScore;
    }

    public void setIndividualScore(double individualScore) {
        this.individualScore = individualScore;
    }

    @Basic
    @Column(name = "individualTime")
    public Timestamp getIndividualTime() {
        return individualTime;
    }

    public void setIndividualTime(Timestamp individualTime) {
        this.individualTime = individualTime;
    }

    @Basic
    @Column(name = "groupNumber")
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

        PracticescoreEntity that = (PracticescoreEntity) o;

        if (Double.compare(that.individualScore, individualScore) != 0) return false;
        if (groupNumber != that.groupNumber) return false;
        if (courseId != null ? !courseId.equals(that.courseId) : that.courseId != null) return false;
        if (classId != null ? !classId.equals(that.classId) : that.classId != null) return false;
        if (practiceName != null ? !practiceName.equals(that.practiceName) : that.practiceName != null) return false;
        if (studentNumber != null ? !studentNumber.equals(that.studentNumber) : that.studentNumber != null)
            return false;
        if (individualTime != null ? !individualTime.equals(that.individualTime) : that.individualTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = courseId != null ? courseId.hashCode() : 0;
        result = 31 * result + (classId != null ? classId.hashCode() : 0);
        result = 31 * result + (practiceName != null ? practiceName.hashCode() : 0);
        result = 31 * result + (studentNumber != null ? studentNumber.hashCode() : 0);
        temp = Double.doubleToLongBits(individualScore);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (individualTime != null ? individualTime.hashCode() : 0);
        result = 31 * result + groupNumber;
        return result;
    }
}
