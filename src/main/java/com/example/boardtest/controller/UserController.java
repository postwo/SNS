package com.example.boardtest.controller;

import com.example.boardtest.model.user.UserSingUpRequestBody;
import com.example.boardtest.model.user.User;
import com.example.boardtest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<User> singup(@Valid @RequestBody UserSingUpRequestBody userSingupReqestBody){
        var user = userService.signUp(userSingupReqestBody.username(),userSingupReqestBody.password());
        return ResponseEntity.ok(user);
//        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
