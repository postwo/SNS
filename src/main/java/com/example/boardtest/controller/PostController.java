package com.example.boardtest.controller;

import com.example.boardtest.model.Post;
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
        List<Post> posts = postService.getPosts();
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
        Post post = postService.createPost(postPostRequestBody);
        return ResponseEntity.ok(post);
    }

}
