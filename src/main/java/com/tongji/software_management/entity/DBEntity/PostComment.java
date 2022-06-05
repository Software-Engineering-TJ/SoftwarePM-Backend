package com.tongji.software_management.entity.DBEntity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "post_comment", schema = "education", catalog = "")
public class PostComment {
    private int id;
    private int postId;
    private String studentNumber;
    private String content;
    private Timestamp commentTime;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "post_id")
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "comment_time")
    public Timestamp getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostComment that = (PostComment) o;
        return id == that.id && postId == that.postId && Objects.equals(studentNumber, that.studentNumber) && Objects.equals(content, that.content) && Objects.equals(commentTime, that.commentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postId, studentNumber, content, commentTime);
    }
}
