package com.tongji.software_management.entity.DBEntity;

import com.tongji.software_management.entity.LogicalEntity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Administrator extends User {
    private String adminNumber;
//    private String email;
//    private String password;
//    private String name;

    @Id
    @Column(name = "admin_number")
    public String getAdminNumber() {
        return adminNumber;
    }

    public void setAdminNumber(String adminNumber) {
        this.adminNumber = adminNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Administrator that = (Administrator) o;
        return Objects.equals(adminNumber, that.adminNumber) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminNumber, email, password, name);
    }

    @Override
    public String getUserNumber() {
        return adminNumber;
    }

    @Override
    public void setUserNumber(String userNumber){}

    @Override
    public int getSex() {
        return 1; //应该用不到，默认男
    }

    @Override
    public void setSex(int sex) {

    }

    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {

    }

    @Override
    public int getStatus() {
        return 1; //默认已经激活
    }

    @Override
    public void setStatus(int status) {

    }

    @Override
    public String getStudentNumber() {
        return null;
    }

    @Override
    public void setStudentNumber(String studentNumber) {

    }

    @Override
    public String getInstructorNumber() {
        return null;
    }

    @Override
    public void setInstructorNumber(String phoneNumber) {

    }
}
