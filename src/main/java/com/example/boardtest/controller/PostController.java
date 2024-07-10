package com.example.boardtest.controller;

import com.example.boardtest.model.entity.UserEntity;
import com.example.boardtest.model.post.Post;
import com.example.boardtest.model.post.PostPatchRequestBody;
import com.example.boardtest.model.post.PostPostRequestBody;
import com.example.boardtest.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    //전체조회
    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
        logger.info("GET /API/v1/posts");
        var posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    //단건조회
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable("postId") Long postId){
        logger.info("GET /API/v1/posts/{}",postId);
        var post = postService.getPostByPostId(postId);
        return ResponseEntity.ok(post);
    }

    //게시물 생성 POST /posts
    //@RequestBody 가 정상적으로 동작하기 위해서는 빈생성자가 있어야 한다
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody, Authentication authentication){
        logger.info("Post /API/v1/posts");
        var post = postService.createPost(postPostRequestBody,(UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }



    //단건 단위로 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<Post> udpatePost(@PathVariable("postId") Long postId, @RequestBody PostPatchRequestBody postPatchRequestBody,
                                           Authentication authentication){
        logger.info("Path /API/v1/posts/{}",postId);
        var post = postService.updatePost(postId,postPatchRequestBody,(UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(post);
    }

    //게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId,Authentication authentication){
        logger.info("DELETE /API/v1/posts/{}",postId);
        postService.deletePost(postId,(UserEntity)authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }


    //좋아요
    @PostMapping("/{postId}/likes")
    public ResponseEntity<Post> toggleLike(@PathVariable Long postId, Authentication authentication) {
        System.out.println("여기");
        var post = postService.toggleLike(postId, (UserEntity) authentication.getPrincipal());
        return new ResponseEntity<>(post, HttpStatus.OK);
    }


}