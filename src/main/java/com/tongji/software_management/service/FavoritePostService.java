package com.tongji.software_management.service;

import com.tongji.software_management.dao.FavoritePostRepository;
import com.tongji.software_management.dao.PostRepository;
import com.tongji.software_management.dao.StudentRepository;
import com.tongji.software_management.entity.DBEntity.FavoritePost;
import com.tongji.software_management.entity.DBEntity.Post;
import com.tongji.software_management.entity.DBEntity.Student;
import com.tongji.software_management.entity.LogicalEntity.FavoritePostDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritePostService {
    @Autowired
    FavoritePostRepository favoritePostRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    PostRepository postRepository;

    public int collectPost(FavoritePost favoritePost){
        return favoritePostRepository.saveAndFlush(favoritePost).getId();
    }

    public void deleteById(int favoriteId){
        favoritePostRepository.deleteById(favoriteId);
    }

    public List<FavoritePost> findByStudentNumberAndLimit(String studentNumber,int pageNumber, int pageSize){
        Sort sort = Sort.by("id").descending();

        return favoritePostRepository.findByStudentNumber(studentNumber, PageRequest.of(pageNumber-1,pageSize,sort)).getContent();
    }

    public FavoritePostDTO convertToFavoritePostDTO(FavoritePost favoritePost){
        FavoritePostDTO favoritePostDTO = new FavoritePostDTO();
        BeanUtils.copyProperties(favoritePost,favoritePostDTO);

        Post post = postRepository.findById(favoritePost.getPostId()).get();
        favoritePostDTO.setPostTitle(post.getTitle());
        favoritePostDTO.setPostType(post.getType());

        Student student = studentRepository.findStudentByStudentNumber(post.getStudentNumber());
        favoritePostDTO.setStudentName(student.getName());

        return favoritePostDTO;
    }

    public int getCountOfFavoritePost(String studentNumber){
        return favoritePostRepository.countByStudentNumber(studentNumber);
    }

}
