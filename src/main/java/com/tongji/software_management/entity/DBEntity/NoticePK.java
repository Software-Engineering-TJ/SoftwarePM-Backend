package com.tongji.software_management.entity.DBEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class NoticePK implements Serializable {
    private String courseId;
    private String classId;
    private String date;

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

    @Column(name = "date")
    @Id
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoticePK noticePK = (NoticePK) o;
        return Objects.equals(courseId, noticePK.courseId) && Objects.equals(classId, noticePK.classId) && Objects.equals(date, noticePK.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, classId, date);
    }
}
