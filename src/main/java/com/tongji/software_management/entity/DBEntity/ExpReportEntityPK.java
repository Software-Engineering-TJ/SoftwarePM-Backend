package com.tongji.software_management.entity.DBEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class ExpreportEntityPK implements Serializable {
    private String courseId;
    private String expname;
    private String classId;
    private String reportName;

    @Column(name = "courseID")
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

    @Column(name = "classID")
    @Id
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Column(name = "reportName")
    @Id
    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpreportEntityPK that = (ExpreportEntityPK) o;

        if (courseId != null ? !courseId.equals(that.courseId) : that.courseId != null) return false;
        if (expname != null ? !expname.equals(that.expname) : that.expname != null) return false;
        if (classId != null ? !classId.equals(that.classId) : that.classId != null) return false;
        if (reportName != null ? !reportName.equals(that.reportName) : that.reportName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = courseId != null ? courseId.hashCode() : 0;
        result = 31 * result + (expname != null ? expname.hashCode() : 0);
        result = 31 * result + (classId != null ? classId.hashCode() : 0);
        result = 31 * result + (reportName != null ? reportName.hashCode() : 0);
        return result;
    }
}
