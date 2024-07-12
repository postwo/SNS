package com.example.boardtest.exception.follow;

import com.example.boardtest.exception.ClientErrorException;
import com.example.boardtest.model.entity.UserEntity;
import org.springframework.http.HttpStatus;

public class FollowAlreadyExistsException extends ClientErrorException {

    public FollowAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "Follow already exists.");
    }

    public FollowAlreadyExistsException(UserEntity follower, UserEntity following) {
        super(
                HttpStatus.CONFLICT,
                "Follow with follower "
                        + follower.getUsername()
                        + " and following "
                        + following.getUsername()
                        + " already exists.");
    }
}
