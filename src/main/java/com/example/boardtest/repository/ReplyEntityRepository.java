package com.example.boardtest.repository;


import com.example.boardtest.model.entity.PostEntity;
import com.example.boardtest.model.entity.ReplyEntity;
import com.example.boardtest.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyEntityRepository extends JpaRepository<ReplyEntity,Long> {

    //유저정보 기반으로 게시물 조회
    List<ReplyEntity> findByUser(UserEntity user);

    //특정 게시물 에있는 모든 댓글을 찾아온다
    List<ReplyEntity> findByPost(PostEntity post);
}
