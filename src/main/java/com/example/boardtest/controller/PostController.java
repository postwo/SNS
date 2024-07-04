package com.example.boardtest.controller;

import com.example.boardtest.model.Post;
import com.example.boardtest.model.PostPatchRequestBody;
import com.example.boardtest.model.PostPostRequestBody;
import com.example.boardtest.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody){
        logger.info("Post /API/v1/posts");
        var post = postService.createPost(postPostRequestBody);
        return ResponseEntity.ok(post);
    }



    //단건 단위로 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<Post> udpatePost(@PathVariable("postId") Long postId, @RequestBody PostPatchRequestBody postPatchRequestBody){
        logger.info("Path /API/v1/posts/{}",postId);
        var post = postService.updatePost(postId,postPatchRequestBody);
        return ResponseEntity.ok(post);
    }

    //게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId){
        logger.info("DELETE /API/v1/posts/{}",postId);
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

}
