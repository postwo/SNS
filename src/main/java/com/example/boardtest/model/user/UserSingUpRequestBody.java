package com.example.boardtest.model.user;


import jakarta.validation.constraints.NotEmpty;

//record는 dto랑 같은 기능을한다
public record UserSingUpRequestBody(
        @NotEmpty
        String username,
        @NotEmpty
        String password
){ }

