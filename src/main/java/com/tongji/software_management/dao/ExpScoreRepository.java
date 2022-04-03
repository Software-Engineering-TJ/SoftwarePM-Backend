package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.ExpScore;
import com.tongji.software_management.entity.DBEntity.ExpScoreEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpScoreRepository extends JpaRepository<ExpScore, ExpScoreEntityPK> {
}
