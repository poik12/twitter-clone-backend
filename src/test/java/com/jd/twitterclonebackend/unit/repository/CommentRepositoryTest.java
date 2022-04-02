package com.jd.twitterclonebackend.unit.repository;

import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.repository.CommentRepository;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentRepositoryTest extends UnitTestInitData {

    @Autowired
    private CommentRepository underTest;

    @Test
    @DisplayName(value = "Should delete Comment Entity by Tweet Id")
    void should_deleteComment_byTweetId() {
        // given
        UserEntity userEntity = initUserEntity();
        TweetEntity tweetEntity = initTweetEntityWithFiles(userEntity);
        CommentEntity commentEntity = initCommentEntity(userEntity, tweetEntity);
        underTest.save(commentEntity);

        // when
        underTest.deleteByTweetId(tweetEntity.getId());

        // then
        assertTrue(underTest.findAll().isEmpty());
    }

    @Test
    @DisplayName(value = "Should find List<CommentEntity> by TweetEntity and order by CreatedAt Desc")
    void should_findAllCommentEntities_byTweetEntity_andOrderByCreatedAtDesc() {
        // given
        UserEntity userEntity = initUserEntity();
        TweetEntity tweetEntity = initTweetEntityWithFiles(userEntity);
        CommentEntity commentEntity = initCommentEntity(userEntity, tweetEntity);
        underTest.save(commentEntity);
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

        // when
        var result = underTest.findAllByTweetAndOrderByCreatedAtDesc(
                tweetEntity, pageable
        );

        // then
        assertAll(
                () -> {
                    assertEquals(1, result.size());
                }
        );
    }

    @Test
    @DisplayName(value = "Should find List<CommentEntity> by UserEntity and Tweet Id and order by CreatedAt Desc")
    void should_findAllCommentEntities_byUserEntityAndTweetId_andOrderByCreatedAtDesc() {
        // given
        UserEntity userEntity = initUserEntity();
        TweetEntity tweetEntity = initTweetEntityWithFiles(userEntity);
        CommentEntity commentEntity = initCommentEntity(userEntity, tweetEntity);
        underTest.save(commentEntity);
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

        // when
        var result = underTest.findAllByUserAndTweetIdAndOrderByCreatedAtDesc(
                userEntity, tweetEntity.getId(), pageable
        );

        // then
        assertAll(
                () -> {
                    assertEquals(1, result.size());
                }
        );
    }
}