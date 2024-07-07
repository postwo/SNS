package com.example.boardtest.controller;

import com.example.boardtest.model.user.User;
import com.example.boardtest.model.user.UserAuthenticationResponse;
import com.example.boardtest.model.user.UserLoginRequestBody;
import com.example.boardtest.model.user.UserSingUpRequestBody;
import com.example.boardtest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;


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


}
