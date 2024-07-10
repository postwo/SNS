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
    public Reply createReply(Long postId, ReplyPostRequestBody replyPostRequestBody, UserEntity currentUser) {
        //게시물 조회
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()->new PostNotFoundException(postId));

        var replyEntity = replyEntityRepository.save(ReplyEntity.of(replyPostRequestBody.body(),currentUser,postEntity));
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


 
//
//    //게시물 삭제
//    public void deletePost(Long postId,UserEntity currentUser) {
//        var postEntity = postEntityRepository.findById(postId).orElseThrow(
//                ()->new PostNotFoundException(postId));
//
//        if (!postEntity.getUser().equals(currentUser)){
//            throw new UserNotAllowedException();
//        }
//
//        postEntityRepository.delete(postEntity);
//    }
//
//    //특정 유저가 작성한 게시물을 모두 조회
//    public List<Post> getPostsByUsername(String username) {
//        var user =
//                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
//        List<PostEntity> postEntities = postEntityRepository.findByUser(user);
//        return postEntities.stream().map(Post::from).toList();
//    }


}
