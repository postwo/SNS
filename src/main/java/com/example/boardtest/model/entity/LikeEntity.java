package com.example.boardtest.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(
        name = "\"like\"", // 예약된 명령어여서 이렇게 해줘야 사용할수 있다
        indexes = {
                @Index(name = "like_userid_postid_idx", columnList = "userid,postid",unique = true)})
                //좋아요가 2개 이상 생성되는걸 방지하기 위해 unique를 설정
                // 성능개선 = 많이 조회할거를 index를 생성한다

public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Column
    private ZonedDateTime createdDateTime;


    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "postid")
    private PostEntity post;

    public static LikeEntity of( UserEntity user, PostEntity post){
        var reply  = new LikeEntity();
        reply.setUser(user);
        reply.setPost(post);
        return reply;
    }



    //밑의 어노테이션들은 jpa에 의해서 실제 데이터가 내부적으로 저장되기 직전에 혹은 수정되기 직전에 원하는 로직을 수행할수있다
    @PrePersist
    private void prePersist(){
        this.createdDateTime =ZonedDateTime.now();
    }

}
