package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.ExpReport;
import com.tongji.software_management.entity.DBEntity.ExpReportEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ExpReportRepository extends JpaRepository<ExpReport, ExpReportEntityPK> {
    List<ExpReport> findExpReportsByCourseIdAndClassId(String courseID, String classID);
    int deleteExpReportByCourseIdAndClassIdAndExpnameAndReportName(String courseID, String classID, String expname, String reportName);
    @Transactional
    @Modifying
    @Query("update ExpReport er set er.reportName = ?4 , er.reportInfo = ?5 , er.endDate = ?6 , er.fileType = ?7 " +
            "where(er.courseId = ?1 and er.classId = ?2 and er.expname = ?3)")
    int UpdateReportDesc(String courseID, String classID, String expname, String reportName, String reportInfo, String endDate, String fileType);

}
