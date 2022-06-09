package com.tongji.software_management.entity.DBEntity;

import com.tongji.software_management.entity.LogicalEntity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
public class Student extends User {
    private String studentNumber;
//    private String email;
//    private String password;
//    private String name;
    private int sex;
    private String phoneNumber;
    private int status;

    @Id
    @Column(name = "student_number")
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "sex")
    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return sex == student.sex && status == student.status && Objects.equals(studentNumber, student.studentNumber) && Objects.equals(email, student.email) && Objects.equals(password, student.password) && Objects.equals(name, student.name) && Objects.equals(phoneNumber, student.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber, email, password, name, sex, phoneNumber, status);
    }

    @Override
    public String getUserNumber() {
        return studentNumber;
    }

    public void setUserNumber(String userNumber){}

    @Override
    public String getInstructorNumber() {
        return null;
    }

    @Override
    public String getAdminNumber() {
        return null;
    }

    @Override
    public void setInstructorNumber(String phoneNumber) {
    }

    @Override
    public void setAdminNumber(String adminNumber) {
    }
}
