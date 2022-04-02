package com.tongji.software_management.entity.DBEntity;

import javax.persistence.*;

@Entity
@Table(name = "experiment", schema = "education", catalog = "")
@IdClass(ExperimentEntityPK.class)
public class ExperimentEntity {
    private String courseId;
    private String expname;
    private String classId;
    private String startDate;
    private String endDate;
    private String expInfo;

    @Id
    @Column(name = "courseID")
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Id
    @Column(name = "expname")
    public String getExpname() {
        return expname;
    }

    public void setExpname(String expname) {
        this.expname = expname;
    }

    @Id
    @Column(name = "classID")
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Basic
    @Column(name = "startDate")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "endDate")
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "expInfo")
    public String getExpInfo() {
        return expInfo;
    }

    public void setExpInfo(String expInfo) {
        this.expInfo = expInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExperimentEntity that = (ExperimentEntity) o;

        if (courseId != null ? !courseId.equals(that.courseId) : that.courseId != null) return false;
        if (expname != null ? !expname.equals(that.expname) : that.expname != null) return false;
        if (classId != null ? !classId.equals(that.classId) : that.classId != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (expInfo != null ? !expInfo.equals(that.expInfo) : that.expInfo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = courseId != null ? courseId.hashCode() : 0;
        result = 31 * result + (expname != null ? expname.hashCode() : 0);
        result = 31 * result + (classId != null ? classId.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (expInfo != null ? expInfo.hashCode() : 0);
        return result;
    }
}
