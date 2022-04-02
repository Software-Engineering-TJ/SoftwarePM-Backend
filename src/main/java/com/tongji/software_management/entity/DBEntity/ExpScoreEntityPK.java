package com.tongji.software_management.entity.DBEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class ExpscoreEntityPK implements Serializable {
    private String studentNumber;
    private String courseId;
    private String expname;
    private String classId;

    @Column(name = "studentNumber")
    @Id
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    @Column(name = "courseID")
    @Id
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Column(name = "expname")
    @Id
    public String getExpname() {
        return expname;
    }

    public void setExpname(String expname) {
        this.expname = expname;
    }

    @Column(name = "classID")
    @Id
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpscoreEntityPK that = (ExpscoreEntityPK) o;

        if (studentNumber != null ? !studentNumber.equals(that.studentNumber) : that.studentNumber != null)
            return false;
        if (courseId != null ? !courseId.equals(that.courseId) : that.courseId != null) return false;
        if (expname != null ? !expname.equals(that.expname) : that.expname != null) return false;
        if (classId != null ? !classId.equals(that.classId) : that.classId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = studentNumber != null ? studentNumber.hashCode() : 0;
        result = 31 * result + (courseId != null ? courseId.hashCode() : 0);
        result = 31 * result + (expname != null ? expname.hashCode() : 0);
        result = 31 * result + (classId != null ? classId.hashCode() : 0);
        return result;
    }
}
