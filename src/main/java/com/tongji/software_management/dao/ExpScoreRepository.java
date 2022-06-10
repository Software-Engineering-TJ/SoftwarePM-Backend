package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.ExpScore;
import com.tongji.software_management.entity.DBEntity.ExpScorePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ExpScoreRepository extends JpaRepository<ExpScore, ExpScorePK> {
    ExpScore findExpScoreByCourseIdAndClassIdAndExpnameAndStudentNumber(String courseID, String classID, String expname, String studentNumber);
    List<ExpScore> findExpScoresByCourseIdAndExpnameAndClassId(String courseID, String expname, String classID);
    @Transactional
    int deleteExpScoreByFileUrl(String fileUrl);

    @Query("select es from ExpScore es where (es.courseId = ?1 and es.classId = ?2 and es.expname = ?3) order by es.score DESC")
    List<ExpScore> findExpScoresByCourseIdAndClassIdAndExpnameDESC(String courseID, String classID, String expname);

    @Transactional
    @Modifying
    @Query("update ExpScore es set es.score = ?5,es.comment = ?6 where (es.studentNumber = ?1 and " +
            "es.courseId = ?2 and es.expname = ?3 and es.classId = ?4)")
    int UpdateExpScore(String studentNumber, String courseID, String expname, String classID, double score, String comment);

    @Transactional
    @Modifying
    @Query("update ExpScore es set es.fileUrl = ?5 where (es.courseId = ?1 and " +
            "es.classId = ?2 and es.expname = ?3 and es.studentNumber = ?4)")
    int UpdateFileUrl(String courseID, String classID, String expname, String studentNumber, String fileUrl);

}
