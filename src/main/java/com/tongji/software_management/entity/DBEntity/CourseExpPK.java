package com.tongji.software_management.entity.DBEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class CourseExpPK implements Serializable {
    private String courseId;
    private String expname;

    @Column(name = "course_id")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseExpPK that = (CourseExpPK) o;
        return Objects.equals(courseId, that.courseId) && Objects.equals(expname, that.expname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, expname);
    }
}
