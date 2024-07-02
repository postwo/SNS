package com.example.boardtest.service;

import com.example.boardtest.model.Post;
import com.example.boardtest.model.PostPostRequestBody;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    //단일
    public Optional<Post> getPostByPostId(Long postId){
        return posts.stream().filter(post -> postId.equals(post.getPostId())).findFirst();
    }

    //게시물 생성
    public Post createPost(PostPostRequestBody postPostRequestBody) {
       Long newPostId = posts.stream().mapToLong(Post::getPostId).max().orElse(0L) +1;

       Post newPost = new Post(newPostId,postPostRequestBody.body(),ZonedDateTime.now());
       posts.add(newPost);

       return newPost;
    }
}
