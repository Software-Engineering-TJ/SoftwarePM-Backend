package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Course findCourseByCourseId(String courseID);
    List<Course> findCourseByInstructorNumber(String instructorNumber);
    List<Course> findCoursesByFlag(int flag);
    int deleteCourseByCourseId(String courseID);
    @Transactional
    @Modifying
    @Query("update Course c set c.instructorNumber = ?2 where (c.courseId = ?1)")
    int SetDutyInstructor(String courseID, String instructorNumber);

    @Transactional
    @Modifying
    @Query("update Course c set c.flag = ?2 where (c.courseId = ?1)")
    int UpdateFlagOfCourseByCourseID(String courseID, int flag);
}
