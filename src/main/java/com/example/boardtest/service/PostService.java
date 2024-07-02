package com.example.boardtest.service;

import com.example.boardtest.model.Post;
import com.example.boardtest.model.PostPatchRequestBody;
import com.example.boardtest.model.PostPostRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    public static List<Post> posts = new ArrayList<>();

    static {
        posts.add(new Post(1L,"Post 1",ZonedDateTime.now()));
        posts.add(new Post(2L,"Post 2", ZonedDateTime.now()));
        posts.add(new Post(3L,"Post 3", ZonedDateTime.now()));
    }

    //목록
    public List<Post> getPosts(){
        return posts;
    }

    //단건
    public Optional<Post> getPostByPostId(Long postId){
        return posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();
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
