package com.jd.twitterclonebackend.integration.service;

import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceImplTest extends IntegrationTestInitData {

    @BeforeEach
    void setUp() {
        initCurrentLoggedUser();
    }

    @Test
    @DisplayName(value = "Should add new Comment Entity")
    void should_addComment() {
        // given
        CommentRequestDto commentRequestDto = initCommentRequestDto();

        // when
        commentService.addComment(commentRequestDto);

        // then
        List<CommentEntity> commentEntityList = commentRepository.findAll();
        CommentEntity commentEntity = commentEntityList.get(0);

        assertAll(
                () -> {
                    assertEquals(commentEntity.getTweet().getId(), commentRequestDto.getTweetId());
                    assertEquals(commentEntity.getUser().getUsername(), commentRequestDto.getUsername());
                    assertEquals(commentEntity.getText(), commentRequestDto.getText());
                }
        );

    }

    @Test
    @DisplayName(value = "Should get List<CommentResponseDto> by Tweet Id")
    void should_getAllComments_byTweetId() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName(value = "Should get List<CommentResponseDto> (3) by Username and Tweet Id")
    void should_getThreeLastCommentsForTweet_byUsernameAndTweetId() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName(value = "Should delete Comment Entity by Id")
    void should_deleteCommentEntity_byId() {
        // given

        // when

        // then

    }

}