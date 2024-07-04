package com.example.boardtest.service;

import com.example.boardtest.model.Post;
import com.example.boardtest.model.PostPatchRequestBody;
import com.example.boardtest.model.PostPostRequestBody;
import com.example.boardtest.repository.PostEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
       var newPostId = posts.stream().mapToLong(Post::getPostId).max().orElse(0L) +1;
       var newPost = new Post(newPostId,postPostRequestBody.body(),ZonedDateTime.now());
       posts.add(newPost);
       return newPost;
    }

    //단건수정
    public Post updatePost(Long postId, PostPatchRequestBody postPatchRequestBody) {
       Optional<Post> postOptional =  posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

       //해당 게시물이 존재하는지
       if (postOptional.isPresent()){
           Post postToUpdate = postOptional.get();
           postToUpdate.setBody(postPatchRequestBody.body());
           return postToUpdate;
       }else{
          throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not Found");
       }
    }

    //게시물 삭제
    public void deletePost(Long postId) {
        //대상 게시물 먼저 조회
        Optional<Post> postOptional =  posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();

        if (postOptional.isPresent()){
            posts.remove(postOptional.get());
        }else{
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Post not Found");
        }
    }
}
