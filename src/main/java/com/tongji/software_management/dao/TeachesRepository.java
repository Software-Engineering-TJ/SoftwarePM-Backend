package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Teaches;
import com.tongji.software_management.entity.DBEntity.TeachesPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachesRepository extends JpaRepository<Teaches, TeachesPK> {
    List<Teaches> findTeachesByCourseIdAndClassId(String courseID, String classID);
    List<Teaches> findTeachesByInstructorNumber(String instructorNumber);

}
