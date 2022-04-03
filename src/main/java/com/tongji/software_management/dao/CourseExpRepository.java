package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.CourseExp;
import com.tongji.software_management.entity.DBEntity.CourseExpEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseExpRepository extends JpaRepository<CourseExp, CourseExpEntityPK> {
}
