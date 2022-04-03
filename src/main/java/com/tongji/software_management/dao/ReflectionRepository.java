package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Reflection;
import com.tongji.software_management.entity.DBEntity.ReflectionEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReflectionRepository extends JpaRepository<Reflection, ReflectionEntityPK> {
}
