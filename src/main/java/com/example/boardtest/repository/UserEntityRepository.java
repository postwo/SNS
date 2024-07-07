package com.example.boardtest.repository;


import com.example.boardtest.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity>findByUsername(String username);


    //검색  전달받은 username이 부분적으로 포함되는걸 조회
    List<UserEntity> findByUsernameContaining(String username);

}
