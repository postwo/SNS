package com.example.boardtest.controller;

import com.example.boardtest.model.Post;
import com.example.boardtest.model.PostPatchRequestBody;
import com.example.boardtest.model.PostPostRequestBody;
import com.example.boardtest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    //전체조회
    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
        var posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    //단건조회
    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostByPostId(@PathVariable("postId") Long postId){

        Optional<Post> matchingPost = postService.getPostByPostId(postId);

       return matchingPost.map(post-> ResponseEntity.ok(post))
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    //게시물 생성 POST /posts
    //@RequestBody 가 정상적으로 동작하기 위해서는 빈생성자가 있어야 한다
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody postPostRequestBody){
        var post = postService.createPost(postPostRequestBody);
        return ResponseEntity.ok(post);
    }



    //단건 단위로 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<Post> udpatePost(@PathVariable("postId") Long postId, @RequestBody PostPatchRequestBody postPatchRequestBody){
        var post = postService.updatePost(postId,postPatchRequestBody);
        return ResponseEntity.ok(post);
    }

    //게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId){
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

}
