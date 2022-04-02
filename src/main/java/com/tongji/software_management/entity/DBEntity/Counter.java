package com.tongji.software_management.entity.DBEntity;

import javax.persistence.*;

@Entity
@Table(name = "counter", schema = "education", catalog = "")
public class CounterEntity {
    private int courseId;
    private int classId;
    private int id;

    @Basic
    @Column(name = "courseID")
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Basic
    @Column(name = "classID")
    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CounterEntity that = (CounterEntity) o;

        if (courseId != that.courseId) return false;
        if (classId != that.classId) return false;
        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = courseId;
        result = 31 * result + classId;
        result = 31 * result + id;
        return result;
    }
}
