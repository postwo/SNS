package com.example.boardtest.exception.reply;



import com.example.boardtest.exception.ClientErrorException;
import org.springframework.http.HttpStatus;


//게시물이 발견되지 않았을때 예외처리
public class ReplyNotFoundException extends ClientErrorException {
    public ReplyNotFoundException() {
        super(HttpStatus.NOT_FOUND,"Reply Not Found");
    }

    //예외가 발생했을때 postid를 알고 있을경우
    public ReplyNotFoundException(Long postId) {
        super(HttpStatus.NOT_FOUND,"Reply with Replyid" + postId + "Reply Not Found");
    }

    //postid는 모르지만 구체적인 메시지를 남기고 싶다
    public ReplyNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND,message);
    }
}
