package com.example.boardtest.service;

import com.example.boardtest.exception.post.PostNotFoundException;
import com.example.boardtest.exception.reply.ReplyNotFoundException;
import com.example.boardtest.exception.user.UserNotAllowedException;
import com.example.boardtest.model.entity.ReplyEntity;
import com.example.boardtest.model.entity.UserEntity;
import com.example.boardtest.model.reply.Reply;
import com.example.boardtest.model.reply.ReplyPatchRequestBody;
import com.example.boardtest.model.reply.ReplyPostRequestBody;
import com.example.boardtest.repository.PostEntityRepository;
import com.example.boardtest.repository.ReplyEntityRepository;
import com.example.boardtest.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReplyService {

    @Autowired
    private ReplyEntityRepository replyEntityRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;


    //특정 게시물 모든 댓글
    public List<Reply> getRepliesByPostId(Long postId){
        //게시물 조회
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()->new PostNotFoundException(postId));
        var replyEntities = replyEntityRepository.findByPost(postEntity);
        return replyEntities.stream().map(Reply::from).toList();
    }

    //댓글 생성
    @Transactional
    public Reply createReply(Long postId, ReplyPostRequestBody replyPostRequestBody, UserEntity currentUser) {
        //게시물 조회
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()->new PostNotFoundException(postId));

        var replyEntity = replyEntityRepository.save(ReplyEntity.of(replyPostRequestBody.body(),currentUser,postEntity));

        postEntity.setRepliesCount(postEntity.getRepliesCount()+1); //댓글 생성 될때마다 카운트 1증가

        return Reply.from(replyEntity);
    }

    //댓글수정
    public Reply updateReply(Long postId, Long replyId, ReplyPatchRequestBody replyPatchRequestBody, UserEntity currentUser) {
        //게시물 조회
       postEntityRepository.findById(postId).orElseThrow(
                ()->new PostNotFoundException(postId));

        var replyEntity = replyEntityRepository.findById(replyId).orElseThrow(()->new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        replyEntity.setBody(replyPatchRequestBody.body());

        return Reply.from(replyEntityRepository.save(replyEntity));
    }

    //댓글 삭제
    @Transactional
    public void deleteReply(Long postId, Long replyId, UserEntity currentUser) {
        //게시물 조회
       var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()->new PostNotFoundException(postId));

        var replyEntity = replyEntityRepository.findById(replyId).orElseThrow(()->new ReplyNotFoundException(replyId));

        if (!replyEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }



        replyEntityRepository.delete(replyEntity);

        //삭제시 -1
        //0보다 작아지는걸 방지하기 위해 이렇게 사용
        postEntity.setRepliesCount(Math.max(0,postEntity.getRepliesCount()) -1);
        postEntityRepository.save(postEntity);

    }






}
