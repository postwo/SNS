package com.example.boardtest.exception.user;



import com.example.boardtest.exception.ClientErrorException;
import org.springframework.http.HttpStatus;


//게시물이 발견되지 않았을때 예외처리
public class UserNotFoundException extends ClientErrorException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND,"User Not Found");
    }

    //예외가 발생했을때 postid를 알고 있을경우
    public UserNotFoundException(Long userId) {
        super(HttpStatus.NOT_FOUND,"User with userId" + userId + "Post Not Found");
    }


}
