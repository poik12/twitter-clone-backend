package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.FollowerEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<FollowerEntity, Long> {

    // Find followed and following users by their ids
    Optional<FollowerEntity> findByToAndFrom(UserEntity followedUser, UserEntity followingUser);

    void deleteAllByTo(UserEntity userEntity);

    void deleteAllByFrom(UserEntity userEntity);
}
