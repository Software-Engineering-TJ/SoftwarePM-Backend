package com.tongji.software_management.service;

import com.tongji.software_management.dao.PostCommentRepository;
import com.tongji.software_management.dao.StudentRepository;
import com.tongji.software_management.entity.DBEntity.PostComment;
import com.tongji.software_management.entity.DBEntity.Student;
import com.tongji.software_management.entity.LogicalEntity.PostCommentDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCommentService {
    @Autowired
    PostCommentRepository postCommentRepository;

    @Autowired
    StudentRepository studentRepository;

    public int createPostComment(PostComment postComment){
        return postCommentRepository.saveAndFlush(postComment).getId();
    }

    public void deletePostCommentById(int postCommentId){
        postCommentRepository.deleteById(postCommentId);
    }

    public List<PostComment> findCommentListByPostIdAndLimit(int postId,int pageNumber,int pageSize){
        Sort sort = Sort.by("commentTime").descending();
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        return postCommentRepository.findByPostId(postId, pageable).getContent();
    }

    public int getCountOfCommentByPostId(int postId){
        return postCommentRepository.countByPostId(postId);
    }

    public PostCommentDTO convertToPostCommentDTO(PostComment postComment){
        PostCommentDTO postCommentDTO = new PostCommentDTO();
        BeanUtils.copyProperties(postComment,postCommentDTO);
        Student student = studentRepository.findStudentByStudentNumber(postComment.getStudentNumber());
        postCommentDTO.setStudentName(student.getName());

        return postCommentDTO;
    }

}
