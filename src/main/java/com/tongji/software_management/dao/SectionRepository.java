package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Section;
import com.tongji.software_management.entity.DBEntity.SectionEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, SectionEntityPK> {
}
