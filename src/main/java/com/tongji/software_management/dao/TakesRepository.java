package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Takes;
import com.tongji.software_management.entity.DBEntity.TakesEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TakesRepository extends JpaRepository<Takes, TakesEntityPK> {
    List<Takes> findTakesByStudentNumber(String studentNumber);
    List<Takes> findTakesByCourseIdAndClassId(String courseID, String classID);
    Takes findTakesByCourseIdAndClassIdAndStudentNumber(String courseID, String classID, String studentNumber);

//    @Query("select t from Takes t where t.courseId = ?1 and t.classId = ?2 and t.studentNumber not in (select asc.studentNumber from AttendScore asc where asc.courseId = ?1 and asc.classId = ?2 and asc.title = ?3)")
//    List<Takes> findTakesNotInAttendScore(String courseID, String classID, String title);

    @Transactional
    @Modifying
    @Query("update Takes t set t.status = ?4 where (t.studentNumber = ?1) and (t.courseId = ?2) and (t.classId = ?3)")
    int SetDuty(String studentNumber,String courseID,String classID,Integer newduty);

}
