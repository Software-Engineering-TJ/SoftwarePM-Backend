package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Takes;
import com.tongji.software_management.entity.DBEntity.TakesEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakesRepository extends JpaRepository<Takes, TakesEntityPK> {
}
