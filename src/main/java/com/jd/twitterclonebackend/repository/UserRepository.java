package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmailAddress(String emailAddress);

//    @Modifying
//    @Query(value = "delete from UserEntity u where u.likedPosts = :postId")
//    void deleteLikedPostsByPostId(Long postId);
}
