package com.tongji.software_management.entity.DBEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class PracticescoreEntityPK implements Serializable {
    private String courseId;
    private String classId;
    private String practiceName;
    private String studentNumber;

    @Column(name = "courseID")
    @Id
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Column(name = "classID")
    @Id
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Column(name = "practiceName")
    @Id
    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    @Column(name = "studentNumber")
    @Id
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PracticescoreEntityPK that = (PracticescoreEntityPK) o;

        if (courseId != null ? !courseId.equals(that.courseId) : that.courseId != null) return false;
        if (classId != null ? !classId.equals(that.classId) : that.classId != null) return false;
        if (practiceName != null ? !practiceName.equals(that.practiceName) : that.practiceName != null) return false;
        if (studentNumber != null ? !studentNumber.equals(that.studentNumber) : that.studentNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = courseId != null ? courseId.hashCode() : 0;
        result = 31 * result + (classId != null ? classId.hashCode() : 0);
        result = 31 * result + (practiceName != null ? practiceName.hashCode() : 0);
        result = 31 * result + (studentNumber != null ? studentNumber.hashCode() : 0);
        return result;
    }
}
