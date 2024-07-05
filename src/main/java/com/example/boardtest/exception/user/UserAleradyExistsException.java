package com.example.boardtest.exception.user;

import com.example.boardtest.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserAleradyExistsException extends ClientErrorException {

    public UserAleradyExistsException() {
        super(HttpStatus.CONFLICT,"User alerdy exits");
    }

    //예외가 발생했을때 postid를 알고 있을경우
    public UserAleradyExistsException(Long userId) {
        super(HttpStatus.CONFLICT,"User with username" + userId + "Post alerdy exits");
    }

}
