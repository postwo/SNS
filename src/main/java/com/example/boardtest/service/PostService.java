package com.example.boardtest.service;

import com.example.boardtest.exception.post.PostNotFoundException;
import com.example.boardtest.exception.user.UserNotAllowedException;
import com.example.boardtest.exception.user.UserNotFoundException;
import com.example.boardtest.model.entity.LikeEntity;
import com.example.boardtest.model.entity.PostEntity;
import com.example.boardtest.model.entity.UserEntity;
import com.example.boardtest.model.post.Post;
import com.example.boardtest.model.post.PostPatchRequestBody;
import com.example.boardtest.model.post.PostPostRequestBody;
import com.example.boardtest.repository.LikeEntityRepository;
import com.example.boardtest.repository.PostEntityRepository;
import com.example.boardtest.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostEntityRepository postEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private LikeEntityRepository likeEntityRepository;




    //목록
    public List<Post> getPosts(){
        var postEntities = postEntityRepository.findAll();
        return postEntities.stream().map(Post::from).toList();
    }

    //단건
    public Post getPostByPostId(Long postId){
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()->new PostNotFoundException(postId));
        return Post.from(postEntity);
    }

    //게시물 생성
    public Post createPost(PostPostRequestBody postPostRequestBody, UserEntity currnetUser) {
        var postEntity = postEntityRepository.save(
                PostEntity.of(postPostRequestBody.body(), currnetUser));
        return Post.from(postEntity);
    }


    //단건수정
    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody,UserEntity currentUser) {

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()->new PostNotFoundException(postId));

        if (!postEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        postEntity.setBody(postPatchRequestBody.body());
        var updatedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(updatedPostEntity);
    }

    //게시물 삭제
    public void deletePost(Long postId,UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()->new PostNotFoundException(postId));

        if (!postEntity.getUser().equals(currentUser)){
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }

    //특정 유저가 작성한 게시물을 모두 조회
    public List<Post> getPostsByUsername(String username) {
        var user =
                userEntityRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        List<PostEntity> postEntities = postEntityRepository.findByUser(user);
        return postEntities.stream().map(Post::from).toList();
    }

    //좋아요
    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {
        PostEntity postEntity = postEntityRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        var likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);

        if (likeEntity.isPresent()) {
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikesCount(Math.max(0, (postEntity.getLikesCount() == null ? 0 : postEntity.getLikesCount()) - 1));
        } else {
            likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
            postEntity.setLikesCount((postEntity.getLikesCount() == null ? 0 : postEntity.getLikesCount()) + 1);
        }

        return Post.from(postEntityRepository.save(postEntity));
    }
}
