package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.AttendEntityPK;
import com.tongji.software_management.entity.DBEntity.AttendScore;
import com.tongji.software_management.entity.DBEntity.AttendScoreEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendScoreRepository extends JpaRepository<AttendScore, AttendScoreEntityPK> {

}
