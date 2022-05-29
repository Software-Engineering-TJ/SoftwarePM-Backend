package com.tongji.software_management.entity.DBEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ExpReportPK implements Serializable {
    private String courseId;
    private String expname;
    private String classId;
    private String reportName;

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

    @Column(name = "class_id")
    @Id
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Column(name = "report_name")
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
        ExpReportPK that = (ExpReportPK) o;
        return Objects.equals(courseId, that.courseId) && Objects.equals(expname, that.expname) && Objects.equals(classId, that.classId) && Objects.equals(reportName, that.reportName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, expname, classId, reportName);
    }
}
