package com.example.boardtest.service;

import com.example.boardtest.model.Post;
import com.example.boardtest.model.PostPatchRequestBody;
import com.example.boardtest.model.PostPostRequestBody;
import com.example.boardtest.model.entity.PostEntity;
import com.example.boardtest.repository.PostEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostEntityRepository postEntityRepository;



    //목록
    public List<Post> getPosts(){
       var postEntities = postEntityRepository.findAll();
       return postEntities.stream().map(Post::from).toList();
    }

    //단건
    public Post getPostByPostId(Long postId){
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not Found"));
        return Post.from(postEntity);
    }

    //게시물 생성
    public Post createPost(PostPostRequestBody postPostRequestBody) {
       var postEntity  = new PostEntity();
       postEntity.setBody(postPostRequestBody.body());
       var savedPostEntity = postEntityRepository.save(postEntity);
       return Post.from(savedPostEntity);
    }

    //단건수정
    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody) {

        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not Found"));

        postEntity.setBody(postPatchRequestBody.body());
        var updatedPostEntity = postEntityRepository.save(postEntity);
        return Post.from(updatedPostEntity);
    }

    //게시물 삭제
    public void deletePost(Long postId) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not Found"));

        postEntityRepository.delete(postEntity);
    }
}
