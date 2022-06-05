package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    Page<Post> findByCourseIdAndClassIdAndType(String courseId, String classID, int type, Pageable pageable);

    Page<Post> findByCourseIdAndClassId(String courseId, String classID, Pageable pageable);

    int countByCourseIdAndClassIdAndType(String courseId, String classId, int type);

    int countByCourseIdAndClassId(String courseId, String classId);
}
