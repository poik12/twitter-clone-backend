package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.domain.PostEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("FROM PostEntity WHERE user = :userEntity ORDER BY createdAt DESC")
    List<PostEntity> findByUserAndOrderByCreatedAtDesc(@Param("userEntity") UserEntity userEntity);

    List<PostEntity> findAllByOrderByCreatedAtDesc();
}
