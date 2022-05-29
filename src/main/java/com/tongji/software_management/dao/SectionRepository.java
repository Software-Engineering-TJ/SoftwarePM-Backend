package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Section;
import com.tongji.software_management.entity.DBEntity.SectionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, SectionPK> {
    Section findSectionByCourseIdAndClassId(String courseID,String classID);
    List<Section> findSectionByCourseId(String courseID);


}
