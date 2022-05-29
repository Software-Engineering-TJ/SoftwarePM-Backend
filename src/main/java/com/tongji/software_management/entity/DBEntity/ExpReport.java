package com.tongji.software_management.entity.DBEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ExpReportPK.class)
public class ExpReport {
    private String courseId;
    private String expname;
    private String classId;
    private String reportName;
    private String reportInfo;
    private String fileType;
    private String startDate;
    private String endDate;

    @Id
    @Column(name = "course_id")
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
    @Column(name = "class_id")
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Id
    @Column(name = "report_name")
    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @Basic
    @Column(name = "report_info")
    public String getReportInfo() {
        return reportInfo;
    }

    public void setReportInfo(String reportInfo) {
        this.reportInfo = reportInfo;
    }

    @Basic
    @Column(name = "file_type")
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Basic
    @Column(name = "start_date")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "end_date")
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpReport expreport = (ExpReport) o;
        return Objects.equals(courseId, expreport.courseId) && Objects.equals(expname, expreport.expname) && Objects.equals(classId, expreport.classId) && Objects.equals(reportName, expreport.reportName) && Objects.equals(reportInfo, expreport.reportInfo) && Objects.equals(fileType, expreport.fileType) && Objects.equals(startDate, expreport.startDate) && Objects.equals(endDate, expreport.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, expname, classId, reportName, reportInfo, fileType, startDate, endDate);
    }
}
