package com.example.boardtest.repository;


import com.example.boardtest.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity,Long> {
    
}
