package com.jd.twitterclonebackend.unit.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.CommentMapper;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest extends UnitTestInitData {

    @InjectMocks
    private CommentMapper commentMapper;

    @Test
    void should_mapFromCommentDto_ToCommentEntity() {
        // given
        UserEntity userEntity = initUserEntity();
        TweetEntity tweetEntity = initTweetEntityWithoutFilesAndHashtags(userEntity);
        CommentRequestDto commentRequestDto = initCommentRequestDto();

        // when
        var result = commentMapper.mapFromDtoToEntity(
                commentRequestDto,
                tweetEntity,
                userEntity
        );

        // then
        assertAll(
                () -> {
                    assertEquals(userEntity, result.getUser());
                    assertEquals(tweetEntity, result.getTweet());
                    assertEquals(commentRequestDto.getText(), result.getText());
                }
        );

    }

    @Test
    void should_mapFromCommentEntity_toCommentResponseDto() {
        // given
        UserEntity userEntity = initUserEntity();
        TweetEntity tweetEntity = initTweetEntityWithoutFilesAndHashtags(userEntity);
        CommentEntity commentEntity = initCommentEntity(userEntity, tweetEntity);

        // when
        var result = commentMapper.mapFromEntityToDto(commentEntity);

        // then
        assertAll(
                () -> {
                    assertEquals(commentEntity.getUser().getName(), result.getName());
                    assertEquals(commentEntity.getUser().getUsername(), result.getUsername());
                    assertEquals(commentEntity.getUser().getProfilePicture(), result.getProfileImage());
                    assertEquals(commentEntity.getTweet().getId(), result.getTweetId());
                    assertEquals(commentEntity.getText(), result.getText());
                    assertEquals(
                            TimeAgo.using(commentEntity.getCreatedAt().toEpochMilli()),
                            result.getTimeOfCreation()
                    );
                }
        );

    }

}