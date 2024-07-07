package com.example.boardtest.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;


@Entity
@Table(name = "post",indexes = {@Index(name = "post_userid_idx",columnList = "userid")})// 성능개선 = 많이 조회할거를 index를 생성한다
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
//이렇게 하면 서비스상에는 존재하지 않지만 데이터베이스 상에는 내부적으로 존재하게 삭제할수 있다
//id 기준으로 데이터베이스에서 삭제하는게 deleteddatetime을 현재시간으로 수정하게 한거다
@SQLDelete(sql="update \"post\" set deleteddatetime = CURRENT_TIMESTAMP where postid = ?")
//삭제 되지 않은 게시물 보기 위해 사용
@SQLRestriction("deleteddatetime IS NULL")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private ZonedDateTime createdDateTime;

    @Column
    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    public static PostEntity of(String body, UserEntity user){
        var post  = new PostEntity();
        post.setBody(body);
        post.setUser(user);
        return post;
    }



    //밑의 어노테이션들은 jpa에 의해서 실제 데이터가 내부적으로 저장되기 직전에 혹은 수정되기 직전에 원하는 로직을 수행할수있다
    @PrePersist
    private void prePersist(){
        this.createdDateTime =ZonedDateTime.now();
        this.updatedDateTime = this.createdDateTime;
    }

    @PreUpdate
    private void preUpdate(){
        this.updatedDateTime = ZonedDateTime.now();
    }
}
