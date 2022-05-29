package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Experiment;
import com.tongji.software_management.entity.DBEntity.ExperimentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, ExperimentPK> {
    Experiment findExperimentByCourseIdAndClassIdAndExpname(String courseID, String classID, String expname);
    List<Experiment> findExperimentsByCourseIdAndClassId(String courseID, String classID);


}
