package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.AttendPK;
import com.tongji.software_management.entity.DBEntity.AttendScore;
import com.tongji.software_management.entity.DBEntity.AttendScorePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendScoreRepository extends JpaRepository<AttendScore, AttendScorePK> {
    List<AttendScore> findAttendScoreByCourseIdAndClassIdAndTitle(String courseID, String classID, String title);
    List<AttendScore> findAttendScoreByCourseIdAndClassIdAndStudentNumber(String courseID, String classID, String studentNumber);
    AttendScore findAttendScoreByCourseIdAndClassIdAndTitleAndStudentNumber(String courseID, String classID, String title, String studentNumber);
}
