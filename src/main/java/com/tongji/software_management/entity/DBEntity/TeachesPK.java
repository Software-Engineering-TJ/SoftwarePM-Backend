package com.tongji.software_management.entity.DBEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TeachesPK implements Serializable {
    private String instructorNumber;
    private String courseId;
    private String classId;

    @Column(name = "instructor_number")
    @Id
    public String getInstructorNumber() {
        return instructorNumber;
    }

    public void setInstructorNumber(String instructorNumber) {
        this.instructorNumber = instructorNumber;
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
        TeachesPK teachesPK = (TeachesPK) o;
        return Objects.equals(instructorNumber, teachesPK.instructorNumber) && Objects.equals(courseId, teachesPK.courseId) && Objects.equals(classId, teachesPK.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructorNumber, courseId, classId);
    }
}
