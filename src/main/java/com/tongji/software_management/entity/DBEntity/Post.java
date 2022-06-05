package com.tongji.software_management.entity.DBEntity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@DynamicInsert
@DynamicUpdate
@Entity
public class Post {
    private int id;
    private String courseId;
    private String classId;
    private String studentNumber;
    private String title;
    private String content;
    private int type;
    private Timestamp date;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "course_id")
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Basic
    @Column(name = "class_id")
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Basic
    @Column(name = "student_number")
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "date")
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && type == post.type && Objects.equals(courseId, post.courseId) && Objects.equals(classId, post.classId) && Objects.equals(studentNumber, post.studentNumber) && Objects.equals(title, post.title) && Objects.equals(content, post.content) && Objects.equals(date, post.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseId, classId, studentNumber, title, content, type, date);
    }
}
