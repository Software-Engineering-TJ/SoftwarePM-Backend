package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
}
