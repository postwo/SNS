package com.example.boardtest.exception.user;



import com.example.boardtest.exception.ClientErrorException;
import org.springframework.http.HttpStatus;


//게시물이 발견되지 않았을때 예외처리
public class UserNotAllowedException extends ClientErrorException {
    public UserNotAllowedException() {
        super(HttpStatus.FORBIDDEN,"User Not Found");
    }





}
