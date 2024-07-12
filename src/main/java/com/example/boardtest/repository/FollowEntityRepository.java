package com.example.boardtest.repository;


import com.example.boardtest.model.entity.FollowEntity;
import com.example.boardtest.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FollowEntityRepository extends JpaRepository<FollowEntity, Long> {
    List<FollowEntity> findByFollower(UserEntity follower);

    List<FollowEntity> findByFollowing(UserEntity following);

    Optional<FollowEntity> findByFollowerAndFollowing(UserEntity user, UserEntity following);
}