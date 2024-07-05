package com.example.boardtest.model.user;

import com.example.boardtest.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

//이걸 적용하면 null값은 json에서 안보여준다
@JsonInclude(JsonInclude.Include.NON_NULL)
public record User(
        Long userId,
        String username,
        String password,
        String profile,
        String description,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime
) {
    public static User from(UserEntity userEntity) {
        return new User(
                userEntity.getUserId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getProfile(),
                userEntity.getDescription(),
                userEntity.getCreatedDateTime(),
                userEntity.getUpdatedDateTime()
        );
    }
}
