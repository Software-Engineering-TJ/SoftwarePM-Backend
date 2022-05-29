package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.CourseExp;
import com.tongji.software_management.entity.DBEntity.CourseExpPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseExpRepository extends JpaRepository<CourseExp, CourseExpPK> {
    List<CourseExp> findCourseExpsByCourseId(String courseID);
    CourseExp findCourseExpByCourseIdAndExpname(String courseID, String expname);
    int deleteCourseExpByCourseId(String courseID);
}
