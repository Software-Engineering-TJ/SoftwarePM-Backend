package com.tongji.software_management.dao;

import com.tongji.software_management.entity.DBEntity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment,Integer> {
    Page<PostComment> findByPostId(int postId, Pageable pageable);

    int countByPostId(int postId);
}
