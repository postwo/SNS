package com.example.boardtest.exception.user;



import com.example.boardtest.exception.ClientErrorException;
import org.springframework.http.HttpStatus;


//게시물이 발견되지 않았을때 예외처리
public class UserNotFoundException extends ClientErrorException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND,"User Not Found");
    }


    public UserNotFoundException(String username) {
        super(HttpStatus.NOT_FOUND, "User with username " + username + " not found.");
    }


}
