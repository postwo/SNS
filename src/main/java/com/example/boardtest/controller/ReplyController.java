package com.example.boardtest.controller;

import com.example.boardtest.model.entity.UserEntity;
import com.example.boardtest.model.reply.Reply;
import com.example.boardtest.model.reply.ReplyPatchRequestBody;
import com.example.boardtest.model.reply.ReplyPostRequestBody;
import com.example.boardtest.service.ReplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")
@Slf4j
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    //댓글 조회
    @GetMapping
    public ResponseEntity<List<Reply>> getRepliesByPostId(@PathVariable Long postId){
        var replies = replyService.getRepliesByPostId(postId);
        return ResponseEntity.ok(replies);
    }


    //댓글 생성
    //@RequestBody 가 정상적으로 동작하기 위해서는 빈생성자가 있어야 한다
    @PostMapping
    public ResponseEntity<Reply> createReply(@PathVariable Long postId,
                                            @RequestBody ReplyPostRequestBody replyPostRequestBody, Authentication authentication){
        var reply = replyService.createReply(postId,replyPostRequestBody,(UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }



    //수정
    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> udpatePost(@PathVariable Long postId,
                                           @PathVariable Long replyId
                                            ,@RequestBody ReplyPatchRequestBody replyPatchRequestBody,
                                           Authentication authentication){
        var reply = replyService.updateReply(postId,replyId,replyPatchRequestBody,(UserEntity)authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }

    //댓글 삭제
    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,Authentication authentication,@PathVariable Long replyId){

        replyService.deleteReply(postId,replyId,(UserEntity)authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }

}
