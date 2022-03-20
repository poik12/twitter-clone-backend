package com.jd.twitterclonebackend.repository;

import com.jd.twitterclonebackend.entity.HashtagEntity;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<TweetEntity, Long> {

    @Query(value = "SELECT t FROM TweetEntity t " +
            "WHERE t.user = :userEntity " +
            "ORDER BY t.createdAt DESC")
    List<TweetEntity> findAllByUserAndOrderByCreatedAtDesc(@Param("userEntity") UserEntity userEntity,
                                                           Pageable pageable);

    List<TweetEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    TweetEntity findByDescription(String description);

    void deleteAllByUser(UserEntity userEntity);

    List<TweetEntity> findByUserLikes(UserEntity userEntity, Pageable pageable);

    @Query(value = "SELECT t FROM TweetEntity t " +
            "JOIN FETCH t.comments c " +
            "WHERE c.user = :userEntity " +
            "ORDER BY t.createdAt DESC")
    List<TweetEntity> findPostByCommentsFromUsername(UserEntity userEntity, Pageable pageable);

    @Query(value = "SELECT t FROM TweetEntity  t " +
            "JOIN t.hashtags h " +
            "WHERE h.value = :hashtag " +
            "ORDER BY t.createdAt DESC")
    List<TweetEntity> findByHashTag(@Param("hashtag") String hashtag);
}
