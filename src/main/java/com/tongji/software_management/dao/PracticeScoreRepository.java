package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.PracticeScore;
import com.tongji.software_management.entity.DBEntity.PracticeScoreEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeScoreRepository extends JpaRepository<PracticeScore, PracticeScoreEntityPK> {
}
