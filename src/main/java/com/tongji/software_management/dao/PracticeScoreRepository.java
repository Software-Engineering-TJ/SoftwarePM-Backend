package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.PracticeScore;
import com.tongji.software_management.entity.DBEntity.PracticeScoreEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeScoreRepository extends JpaRepository<PracticeScore, PracticeScoreEntityPK> {
    @Query("select ps.studentNumber from PracticeScore ps where (ps.courseId = ?1 and ps.classId = ?2 and ps.practiceName = ?3 and ps.groupNumber = ?4) order by ps.individualScore DESC,ps.individualTime ASC")
    List<PracticeScore> findPracticeScoreByGroup(String courseID, String classID, String practiceName, int groupNumber);
    PracticeScore findPracticeScoreByCourseIdAndClassIdAndPracticeNameAndStudentNumber(String courseID, String classID, String practiceName, String studentNumber);

}
