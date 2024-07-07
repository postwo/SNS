package com.example.boardtest.repository;


import com.example.boardtest.model.entity.PostEntity;
import com.example.boardtest.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostEntityRepository extends JpaRepository<PostEntity,Long> {

    //유저정보 기반으로 게시물 조회
    List<PostEntity> findByUser(UserEntity user);
}
