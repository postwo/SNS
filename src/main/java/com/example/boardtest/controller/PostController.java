package com.example.boardtest.controller;

import com.example.boardtest.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {

    @GetMapping("/api/v1/posts")
    public ResponseEntity<List<Post>> getPosts(){
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L,"post 1", ZonedDateTime.now()));
        posts.add(new Post(1L,"post 2", ZonedDateTime.now()));
        posts.add(new Post(1L,"post 3", ZonedDateTime.now()));

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
