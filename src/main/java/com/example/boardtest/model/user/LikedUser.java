package com.example.boardtest.model.user;

import java.time.ZonedDateTime;

public record LikedUser(
        Long userId,
        String username,
        String profile,
        String description,
        Long followersCount,
        Long followingsCount,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        ZonedDateTime likedDateTime,
        Long likedPostId,
        Boolean isFollowing) {

    public static LikedUser from(User user,Long likedPostId,ZonedDateTime likedDateTime) {
        return new LikedUser(
                user.userId(),
                user.username(),
                user.profile(),
                user.description(),
                user.followersCount(),
                user.followingsCount(),
                user.createdDateTime(),
                user.updatedDateTime(),
                likedDateTime,
                likedPostId,
                user.isFollowing());
    }
}
