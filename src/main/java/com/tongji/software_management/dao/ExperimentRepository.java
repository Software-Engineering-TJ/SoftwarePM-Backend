package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Experiment;
import com.tongji.software_management.entity.DBEntity.ExperimentEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, ExperimentEntityPK> {
}
