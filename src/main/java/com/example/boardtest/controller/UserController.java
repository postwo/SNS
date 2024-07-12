package com.example.boardtest.controller;

import com.example.boardtest.model.entity.UserEntity;
import com.example.boardtest.model.post.Post;
import com.example.boardtest.model.user.*;
import com.example.boardtest.service.PostService;
import com.example.boardtest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;


    //회원가입
    @PostMapping
    public ResponseEntity<User> singup(@Valid @RequestBody UserSingUpRequestBody userSingupReqestBody){
        var user = userService.signUp(userSingupReqestBody.username(),userSingupReqestBody.password());
        return ResponseEntity.ok(user);
//        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //로그인
    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
            @Valid @RequestBody UserLoginRequestBody requestBody) {
        var response = userService.login(requestBody.username(), requestBody.password());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    //검색
    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String query){
        var users = userService.getUsers(query);
        return ResponseEntity.ok(users);
    }


    //단건 조회
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username){
        var user = userService.getUser(username);
        return ResponseEntity.ok(user);
    }

    //정보수정
    @PatchMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username, @RequestBody UserPatchRequestBody requestBody, Authentication authentication){
        var user = userService.updateUser(username,requestBody,(UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }


    //특정 유저가 작성한 게시물을 모두 조회
    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username){
        var posts = postService.getPostsByUsername(username);
        return ResponseEntity.ok(posts);
    }



    //follows 추가
    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(@PathVariable String username, Authentication authentication) {
        var user = userService.follow(username, (UserEntity) authentication.getPrincipal());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //follows 취소
    @DeleteMapping("/{username}/follows")
    public ResponseEntity<User> unfollow(
            @PathVariable String username, Authentication authentication) {
        var user = userService.unFollow(username, (UserEntity) authentication.getPrincipal());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }




}
