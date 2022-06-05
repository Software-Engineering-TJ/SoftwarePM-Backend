package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.FavoritePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritePostRepository extends JpaRepository<FavoritePost,Integer> {
    Page<FavoritePost> findByStudentNumber(String studentNumber, Pageable pageable);

    int countByStudentNumber(String studentNumber);
}
