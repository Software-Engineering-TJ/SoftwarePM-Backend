package com.tongji.software_management.entity.DBEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class AttendPK implements Serializable {
    private String courseId;
    private String classId;
    private String title;

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

    @Column(name = "title")
    @Id
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendPK attendPK = (AttendPK) o;
        return Objects.equals(courseId, attendPK.courseId) && Objects.equals(classId, attendPK.classId) && Objects.equals(title, attendPK.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, classId, title);
    }
}
