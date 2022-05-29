package com.tongji.software_management.entity.DBEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class SectionPK implements Serializable {
    private String courseId;
    private String classId;

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
        SectionPK sectionPK = (SectionPK) o;
        return Objects.equals(courseId, sectionPK.courseId) && Objects.equals(classId, sectionPK.classId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, classId);
    }
}
