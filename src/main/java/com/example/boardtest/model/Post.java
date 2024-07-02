package com.example.boardtest.model;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;





@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private  Long postId;
    private String body;
    private ZonedDateTime createdDateTime;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(postId, post.postId) && Objects.equals(body, post.body) && Objects.equals(createdDateTime, post.createdDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, createdDateTime);
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", body='" + body + '\'' +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
