package com.tongji.software_management.service;

import com.tongji.software_management.dao.PostRepository;
import com.tongji.software_management.dao.StudentRepository;
import com.tongji.software_management.entity.DBEntity.Post;
import com.tongji.software_management.entity.DBEntity.Student;
import com.tongji.software_management.entity.LogicalEntity.PostDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    StudentRepository studentRepository;

    public int createPost(Post post){
        post.setDate(null);
        return postRepository.saveAndFlush(post).getId();
    }

    public void deletePostById(int postId){
        postRepository.deleteById(postId);
    }

    public List<Post> findPostListByCourseIdAndClassIdAndTypeAndLimit(String courseId, String classId,
                                                              int type, int pageNumber,int pageSize){
        // 默认按照时间倒叙
        Sort sort = Sort.by("date").descending();
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        // -1：查找全部
        if(type == -1){
            return postRepository.findByCourseIdAndClassId(courseId,classId,pageable).getContent();
        }
        else{
            return postRepository.findByCourseIdAndClassIdAndType(courseId,classId,type, pageable).getContent();
        }
    }

    public int getCountOfPostByCourseIdAndClassIdAndType(String courseId, String classId, int type){
        if(type==-1){
            return postRepository.countByCourseIdAndClassId(courseId,classId);
        }else{
            return postRepository.countByCourseIdAndClassIdAndType(courseId,classId,type);
        }
    }

    public PostDTO convertToPostDTO(Post post){
        PostDTO postDTO = new PostDTO();

        BeanUtils.copyProperties(post,postDTO);

        Student student = studentRepository.findStudentByStudentNumber(post.getStudentNumber());
        postDTO.setStudentName(student.getName());

        return postDTO;
    }
}
