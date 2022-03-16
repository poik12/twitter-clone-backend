package com.jd.twitterclonebackend.unit.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.dto.request.TweetRequestDto;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.JsonMapper;
import com.jd.twitterclonebackend.mapper.TweetMapper;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TweetMapperTest extends UnitTestInitData {

    @InjectMocks
    private TweetMapper tweetMapper;

    @Mock
    private JsonMapper jsonMapper;

    @Test
    void should_mapFromPostRequestDto_toPostEntity() {
        // given
        TweetRequestDto tweetRequestDto = initPostRequestDto();
        String postRequestJson = "";
        UserEntity userEntity = initUserEntity();

        // when
        when(jsonMapper.mapFromJsonToDto(any(), any()))
                .thenReturn(tweetRequestDto);

        var result = tweetMapper.mapFromDtoToEntity(
                postRequestJson,
                userEntity
        );
        System.out.println(result);

        // then
        assertAll(
                () -> {
                    assertEquals(tweetRequestDto.getDescription(), result.getDescription());
                    assertEquals(userEntity, result.getUser());
                    assertEquals(Collections.emptyList(), result.getComments());
                    assertEquals(0L, result.getCommentNo());
                }
        );
    }

    @Test
    void should_mapFromPostEntity_toPostResponseDto_withoutImageFiles() {
        // given
        UserEntity userEntity = initUserEntity();
        TweetEntity tweetEntity = initPostEntity(userEntity);
        Map<Long, byte[]> imageFileMap = new HashMap<>();

        // when
        var result = tweetMapper.mapFromEntityToDto(tweetEntity);
        System.out.println(result);

        // then
        assertAll(
                () -> {
                    assertEquals(tweetEntity.getId(), result.getId());
                    assertEquals(tweetEntity.getUser().getName(), result.getName());
                    assertEquals(tweetEntity.getUser().getUsername(), result.getUsername());
                    assertEquals(tweetEntity.getUser().getDescription(), result.getDescription());
                    assertEquals(tweetEntity.getCommentNo(), result.getCommentNo());
                    assertEquals(tweetEntity.getCreatedAt(), result.getCreatedAt());
                    assertEquals(TimeAgo.using(tweetEntity.getCreatedAt().toInstant().toEpochMilli()),
                            result.getTweetTimeDuration());
                    assertEquals(tweetEntity.getUser().getProfilePicture(), result.getUserProfilePicture());
                }
        );

    }

    // TODO: map post entity to dto with image files
}