package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    void deleteByTweetId(Long tweetId);

    @Query(value = "SELECT c FROM CommentEntity c " +
            "WHERE c.tweet = :tweetEntity " +
            "ORDER BY c.createdAt DESC")
    List<CommentEntity> findAllByTweetAndOrderByCreatedAtDesc(@Param("tweetEntity") TweetEntity tweetEntity,
                                                              Pageable pageable);

    @Query(value = "SELECT c FROM CommentEntity c " +
            "WHERE c.user = :userEntity AND c.tweet.id = :tweetId " +
            "ORDER BY c.createdAt DESC")
    List<CommentEntity> findAllByUserAndTweetIdAndOrderByCreatedAtDesc(@Param("userEntity") UserEntity userEntity,
                                                                       @Param("tweetId") Long tweetId,
                                                                       Pageable pageable);
}
