package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Integer> {
    List<Practice> findPracticesByCourseIdAndClassId(String courseID, String classID);
    Practice findByPracticeId(int practiceId);

}
