package com.tongji.software_management.entity.DBEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TakesPK implements Serializable {
    private String studentNumber;
    private String courseId;
    private String classId;

    @Column(name = "student_number")
    @Id
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    @Column(name = "course_id")
    @Id
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Column(name = "class_id")
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
        TakesPK takesPK = (TakesPK) o;
        return Objects.equals(studentNumber, takesPK.studentNumber) && Objects.equals(courseId, takesPK.courseId) && Objects.equals(classId, takesPK.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber, courseId, classId);
    }
}
