package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Integer> {
    Reference findReferenceByFileUrl(String fileUrl);
    @Transactional
    int deleteReferenceByFileUrl(String fileUrl);
    List<Reference> findReferencesByCourseIdAndClassId(String courseID, String classID);

}
