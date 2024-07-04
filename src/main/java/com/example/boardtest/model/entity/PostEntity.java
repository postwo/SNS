package com.example.boardtest.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;


@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostEntity that)) return false;
        return Objects.equals(postId, that.postId) && Objects.equals(body, that.body) && Objects.equals(createdDateTime, that.createdDateTime) && Objects.equals(updatedDateTime, that.updatedDateTime) && Objects.equals(deletedDateTime, that.deletedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, createdDateTime, updatedDateTime, deletedDateTime);
    }
}
