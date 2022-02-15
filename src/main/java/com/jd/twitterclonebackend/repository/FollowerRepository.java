package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.domain.FollowerEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<FollowerEntity, Long> {

    // User entities by who logged user is followed
    @Query("SELECT e.from FROM FollowerEntity e WHERE e.to = :user")
    List<UserEntity> getAllFollowersByUser(UserEntity user);

    // User entities who logged user follows
    @Query("SELECT e.to FROM FollowerEntity e WHERE e.from = :user")
    List<UserEntity> getAllFollowingsByUser(UserEntity user);

    // Find followed and following users by their ids
    FollowerEntity findByToAndFrom(UserEntity followedUserId, UserEntity followingUserId);
}
