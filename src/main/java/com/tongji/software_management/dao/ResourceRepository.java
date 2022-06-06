package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource,Integer> {
    Page<Resource> findByCourseIdAndClassIdAndType(String courseId, String classId, int type, Pageable pageable);

    Page<Resource> findByCourseIdAndClassId(String courseId, String classId, Pageable pageable);

    int countByCourseIdAndClassIdAndType(String courseId, String classId, int type);

    int countByCourseIdAndClassId(String courseId, String classId);
}
