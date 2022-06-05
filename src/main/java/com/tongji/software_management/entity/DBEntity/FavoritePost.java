package com.tongji.software_management.entity.DBEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "favorite_post", schema = "education", catalog = "")
public class FavoritePost {
    private int id;
    private String studentNumber;
    private int postId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "post_id")
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritePost that = (FavoritePost) o;
        return id == that.id && postId == that.postId && Objects.equals(studentNumber, that.studentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentNumber, postId);
    }
}
