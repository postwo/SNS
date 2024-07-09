package com.example.boardtest.controller;

import com.example.boardtest.model.entity.UserEntity;
import com.example.boardtest.model.post.Post;
import com.example.boardtest.model.post.PostPatchRequestBody;
import com.example.boardtest.model.reply.Reply;
import com.example.boardtest.model.reply.ReplyPostRequestBody;
import com.example.boardtest.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")
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
    public ResponseEntity<Post> createReply(@PathVariable Long postId,
                                            @RequestBody ReplyPostRequestBody replyPostRequestBody, Authentication authentication){
        var reply = replyService.createReply(postId,replyPostRequestBody,(UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(reply);
    }



//    //단건 단위로 수정
//    @PatchMapping("/{postId}")
//    public ResponseEntity<Post> udpatePost(@PathVariable("postId") Long postId, @RequestBody PostPatchRequestBody postPatchRequestBody,
//                                           Authentication authentication){
//        var post = postService.updatePost(postId,postPatchRequestBody,(UserEntity)authentication.getPrincipal());
//        return ResponseEntity.ok(post);
//    }
//
//    //게시물 삭제
//    @DeleteMapping("/{postId}")
//    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId,Authentication authentication){
//        postService.deletePost(postId,(UserEntity)authentication.getPrincipal());
//        return ResponseEntity.noContent().build();
//    }

}
