package com.tongji.software_management.entity.DBEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(TeachesPK.class)
public class Teaches {
    private String instructorNumber;
    private String courseId;
    private String classId;

    @Id
    @Column(name = "instructor_number")
    public String getInstructorNumber() {
        return instructorNumber;
    }

    public void setInstructorNumber(String instructorNumber) {
        this.instructorNumber = instructorNumber;
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
    @Column(name = "class_id")
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
        Teaches teaches = (Teaches) o;
        return Objects.equals(instructorNumber, teaches.instructorNumber) && Objects.equals(courseId, teaches.courseId) && Objects.equals(classId, teaches.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructorNumber, courseId, classId);
    }
}
