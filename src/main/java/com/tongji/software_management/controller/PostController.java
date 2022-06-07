package com.tongji.software_management.controller;

import com.tongji.software_management.entity.DBEntity.FavoritePost;
import com.tongji.software_management.entity.DBEntity.Post;
import com.tongji.software_management.entity.DBEntity.PostComment;
import com.tongji.software_management.entity.LogicalEntity.ApiResult;
import com.tongji.software_management.entity.LogicalEntity.FavoritePostDTO;
import com.tongji.software_management.entity.LogicalEntity.PostCommentDTO;
import com.tongji.software_management.entity.LogicalEntity.PostDTO;
import com.tongji.software_management.service.FavoritePostService;
import com.tongji.software_management.service.PostCommentService;
import com.tongji.software_management.service.PostService;
import com.tongji.software_management.utils.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("post")
public class PostController {
    @Autowired
    PostService postService;

    @Autowired
    PostCommentService postCommentService;

    @Autowired
    FavoritePostService favoritePostService;

    // post-增
    @PostMapping
    public ApiResult createPost(@RequestBody Post post){
        int postId = postService.createPost(post);
        return ApiResultHandler.success(postId);
    }

    // post-删
    @DeleteMapping
    public ApiResult createPost(int postId){
        postService.deletePostById(postId);
        return ApiResultHandler.success(null);
    }

    // 分页查询某个班级的帖子，type可以取值-1（全查）、0（提问）、1（分享）
    @GetMapping("list/{pageNumber}/{pageSize}")
    public ApiResult getPostList(@PathVariable("pageNumber")int pageNumber,
                                 @PathVariable("pageSize")int pageSize,
                                 @RequestParam("courseId")String courseId,
                                 @RequestParam("classId")String classId,
                                 @RequestParam(value = "type",required = false, defaultValue = "-1")int type){
        if(pageNumber < 1)
            return ApiResultHandler.fail("页数不合理");
        if(pageSize < 1)
            return ApiResultHandler.fail("页大小不合适");

        List<Post> postList = postService.findPostListByCourseIdAndClassIdAndTypeAndLimit(courseId,classId,type,pageNumber,pageSize);
        List<PostDTO> postInfoList = new ArrayList<>();
        for(Post post : postList){
            PostDTO postInfo = postService.convertToPostDTO(post);
            postInfoList.add(postInfo);
        }

        int count = postService.getCountOfPostByCourseIdAndClassIdAndType(courseId,classId,type);
        int temp = count/pageSize;
        Integer pageCount = (count%pageSize==0)?temp:(temp+1);

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageCount);
        map.put("postInfoList",postInfoList);

        return ApiResultHandler.success(map);
    }


    /*****************评论********************/
    // 帖子评论-增
    @PostMapping("comment")
    public ApiResult createPostComment(@RequestBody PostComment postComment){
        int postCommentId = postCommentService.createPostComment(postComment);
        return ApiResultHandler.success(postCommentId);
    }

    // 帖子评论-删
    @DeleteMapping("comment")
    public ApiResult deletePostComment(@RequestParam("postCommentId")int postCommentId){
        postCommentService.deletePostCommentById(postCommentId);
        return ApiResultHandler.success(null);
    }

    // 分页查询帖子的评论
    @GetMapping("comment/{pageNumber}/{pageSize}")
    public ApiResult getCommentListOfPost(@RequestParam("postId")int postId,
                                          @PathVariable("pageNumber")int pageNumber,
                                          @PathVariable("pageSize")int pageSize){
        if(pageNumber<1){
            return ApiResultHandler.fail("页号不合法");
        }
        if(pageSize<1){
            return ApiResultHandler.fail("页大小不合法");
        }
        List<PostComment> postCommentList = postCommentService.findCommentListByPostIdAndLimit(postId,pageNumber,pageSize);

        List<PostCommentDTO> postCommentInfoList = new ArrayList<>();
        for(PostComment postComment : postCommentList) {
            PostCommentDTO postCommentInfo = postCommentService.convertToPostCommentDTO(postComment);
            postCommentInfoList.add(postCommentInfo);
        }

        int count = postCommentService.getCountOfCommentByPostId(postId);
        int temp = count/pageSize;
        Integer pageCount = (count%pageSize==0)?temp:(temp+1);

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageCount);
        map.put("postInfoList",postCommentInfoList);

        return ApiResultHandler.success(map);
    }

    // 收藏帖子
    @PostMapping("favorite")
    public ApiResult addFavorite(@RequestBody FavoritePost favoritePost){
        int favoritePostId = favoritePostService.collectPost(favoritePost);
        return ApiResultHandler.success(favoritePostId);
    }

    // 删除某个收藏
    @DeleteMapping("favorite")
    public ApiResult deleteFavorite(@RequestParam("favoriteId")int favoriteId){
        favoritePostService.deleteById(favoriteId);
        return ApiResultHandler.success(null);
    }

    // 分页获取收藏的帖子
    @GetMapping("favorite/{pageNumber}/{pageSize}")
    public ApiResult getFavoritePostList(@RequestParam("studentNumber")String studentNumber,
                                         @PathVariable("pageNumber")int pageNumber,
                                         @PathVariable("pageSize")int pageSize){
        if(pageNumber<1){
            return ApiResultHandler.fail("页号不合法");
        }
        if(pageSize<1){
            return ApiResultHandler.fail("页大小不合法");
        }

        List<FavoritePostDTO> favoritePostInfoList = new ArrayList<>();
        List<FavoritePost> favoritePostList = favoritePostService.findByStudentNumberAndLimit(studentNumber,pageNumber,pageSize);
        for(FavoritePost favoritePost : favoritePostList){
            FavoritePostDTO favoritePostInfo = favoritePostService.convertToFavoritePostDTO(favoritePost);
            favoritePostInfoList.add(favoritePostInfo);
        }

        int count = favoritePostService.getCountOfFavoritePost(studentNumber);
        int temp = count/pageSize;
        int pageCount = (count%pageSize==0)?temp:temp+1;

        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageCount);
        map.put("favoritePostInfoList",favoritePostInfoList);

        return ApiResultHandler.success(favoritePostInfoList);
    }
}
