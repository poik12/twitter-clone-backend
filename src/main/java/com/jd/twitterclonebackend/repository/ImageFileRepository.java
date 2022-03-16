package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.ImageFileEntity;
import com.jd.twitterclonebackend.entity.TweetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFileEntity, Long> {

    ImageFileEntity getByTweet(TweetEntity tweetEntity);

    @Query(value = "SELECT f FROM ImageFileEntity f WHERE f.tweet.id = :tweetId")
    List<ImageFileEntity> findAllByPostId(@Param("tweetId") Long tweetId);

    void deleteByTweetId(Long postId);

}
