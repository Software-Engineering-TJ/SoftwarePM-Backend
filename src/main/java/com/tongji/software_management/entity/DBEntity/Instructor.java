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
@DynamicUpdate
@DynamicInsert
public class Instructor extends User {
    private String instructorNumber;
//    private String email;
//    private String password;
//    private String name;
    private int sex;
    private String phoneNumber;
    private int status;

    @Id
    @Column(name = "instructor_number")
    public String getInstructorNumber() {
        return instructorNumber;
    }

    public void setInstructorNumber(String instructorNumber) {
        this.instructorNumber = instructorNumber;
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
        Instructor that = (Instructor) o;
        return sex == that.sex && status == that.status && Objects.equals(instructorNumber, that.instructorNumber) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(name, that.name) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructorNumber, email, password, name, sex, phoneNumber, status);
    }


    @Override
    public String getUserNumber() {
        return instructorNumber;
    }

    public void setUserNumber(String userNumber){}

    @Override
    public String getStudentNumber() {
        return null;
    }

    @Override
    public void setStudentNumber(String studentNumber) {

    }

    @Override
    public String getAdminNumber() {
        return null;
    }

    @Override
    public void setAdminNumber(String adminNumber) {

    }
}
