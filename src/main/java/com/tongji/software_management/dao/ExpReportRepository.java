package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.ExpReport;
import com.tongji.software_management.entity.DBEntity.ExpReportEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpReportRepository extends JpaRepository<ExpReport, ExpReportEntityPK> {
}
