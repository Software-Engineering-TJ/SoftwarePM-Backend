package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Reflection;
import com.tongji.software_management.entity.DBEntity.ReflectionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReflectionRepository extends JpaRepository<Reflection, ReflectionPK> {
    List<Reflection> findReflectionListByCourseIdAndClassId(String courseID, String classID);
}
