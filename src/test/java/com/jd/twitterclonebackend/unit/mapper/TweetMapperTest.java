package com.jd.twitterclonebackend.unit.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.dto.request.TweetRequestDto;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.HashtagMapper;
import com.jd.twitterclonebackend.mapper.JsonMapper;
import com.jd.twitterclonebackend.mapper.TweetMapper;
import com.jd.twitterclonebackend.service.HashtagService;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TweetMapperTest extends UnitTestInitData {

    @InjectMocks
    private TweetMapper underTest;

    @Mock
    private JsonMapper jsonMapper;
    @Mock
    private HashtagService hashtagService;
    @Mock
    private HashtagMapper hashtagMapper;

    @Test
    @DisplayName(value = "Map from Tweet Request Dto To Tweet Entity")
    void should_mapFromTweetRequestDto_toTweetEntity() {
        // given
        String tweetRequestJson = "";
        TweetRequestDto tweetRequestDto = initPostRequestDto();
        UserEntity userEntity = initUserEntity();

        // when
        when(jsonMapper.mapFromJsonToDto(any(), any()))
                .thenReturn(tweetRequestDto);
        when(hashtagService.checkHashTags(any()))
                .thenReturn(Collections.emptyList());

        var result = underTest.mapFromDtoToEntity(
                tweetRequestJson,
                userEntity
        );

        // then
        assertAll(
                () -> {
                    assertEquals(tweetRequestDto.getDescription(), result.getDescription());
                    assertEquals(userEntity, result.getUser());
                    assertEquals(Collections.emptyList(), result.getComments());
                    assertEquals(0L, result.getCommentNo());
                    assertEquals(Collections.emptyList(), result.getHashtags());
                }
        );
    }

    @Test
    @DisplayName(value = "Map from Tweet Entity To Tweet Response Dto without Image Files and without Hashtags")
    void should_mapFromTweetEntity_toTweetResponseDto_withoutImageFiles() {
        // given
        UserEntity userEntity = initUserEntity();
        TweetEntity tweetEntity = initTweetEntityWithoutFilesAndHashtags(userEntity);

        // when
        when(hashtagMapper.mapFromEntityToStringList(any()))
                .thenReturn(Collections.emptyList());
        var result = underTest.mapFromEntityToDto(tweetEntity);

        // then
        assertAll(
                () -> {
                    assertEquals(tweetEntity.getId(), result.getId());
                    assertEquals(tweetEntity.getDescription(), result.getDescription());
                    assertEquals(tweetEntity.getCreatedAt(), result.getCreatedAt());
                    assertEquals(tweetEntity.getCommentNo(), result.getCommentNo());
                    assertEquals(tweetEntity.getUserLikes().size(), result.getLikeNo());
                    assertEquals(TimeAgo.using(tweetEntity.getCreatedAt().toInstant().toEpochMilli()),
                            result.getTweetTimeDuration());
                    assertEquals(tweetEntity.getUser().getUsername(), result.getUsername());
                    assertEquals(tweetEntity.getUser().getName(), result.getName());
                    assertEquals(tweetEntity.getUser().getProfilePicture(),
                            result.getUserProfilePicture());
                    assertFalse(result.isLikedByLoggedUser());
                    assertEquals(tweetEntity.getImages(), result.getFileContent());
                    assertEquals(hashtagMapper.mapFromEntityToStringList(tweetEntity.getHashtags()),
                            result.getHashtags());
                }
        );
    }

}