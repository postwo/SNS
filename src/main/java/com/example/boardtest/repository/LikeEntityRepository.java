package com.example.boardtest.repository;


import com.example.boardtest.model.entity.LikeEntity;
import com.example.boardtest.model.entity.PostEntity;
import com.example.boardtest.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity,Long> {


    List<LikeEntity> findByUser(UserEntity user);


    List<LikeEntity> findByPost(PostEntity post);

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
}
