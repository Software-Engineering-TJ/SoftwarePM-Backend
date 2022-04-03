package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Practice;
import com.tongji.software_management.entity.DBEntity.PracticeEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, PracticeEntityPK> {
}
