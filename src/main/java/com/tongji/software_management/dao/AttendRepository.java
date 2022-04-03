package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Attend;
import com.tongji.software_management.entity.DBEntity.AttendEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendRepository extends JpaRepository<Attend, AttendEntityPK> {
    List<Attend> findAttendsByCourseIdAndClassId(String courseId, String classId);
}
