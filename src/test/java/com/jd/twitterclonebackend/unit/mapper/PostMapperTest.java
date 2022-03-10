package com.jd.twitterclonebackend.unit.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.dto.request.PostRequestDto;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.JsonMapper;
import com.jd.twitterclonebackend.mapper.PostMapper;
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

class PostMapperTest extends UnitTestInitData {

    @InjectMocks
    private PostMapper postMapper;

    @Mock
    private JsonMapper jsonMapper;

    @Test
    void should_mapFromPostRequestDto_toPostEntity() {
        // given
        PostRequestDto postRequestDto = initPostRequestDto();
        String postRequestJson = "";
        UserEntity userEntity = initUserEntity();

        // when
        when(jsonMapper.mapFromJsonToDto(any(), any()))
                .thenReturn(postRequestDto);

        var result = postMapper.mapFromDtoToEntity(
                postRequestJson,
                userEntity
        );
        System.out.println(result);

        // then
        assertAll(
                () -> {
                    assertEquals(postRequestDto.getDescription(), result.getDescription());
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
        PostEntity postEntity = initPostEntity(userEntity);
        Map<Long, byte[]> imageFileMap = new HashMap<>();

        // when
        var result = postMapper.mapFromEntityToDto(
                postEntity,
                imageFileMap
        );
        System.out.println(result);

        // then
        assertAll(
                () -> {
                    assertEquals(postEntity.getId(), result.getId());
                    assertEquals(postEntity.getUser().getName(), result.getName());
                    assertEquals(postEntity.getUser().getUsername(), result.getUsername());
                    assertEquals(postEntity.getUser().getDescription(), result.getDescription());
                    assertEquals(postEntity.getCommentNo(), result.getCommentNo());
                    assertEquals(postEntity.getCreatedAt(), result.getCreatedAt());
                    assertEquals(TimeAgo.using(postEntity.getCreatedAt().toInstant().toEpochMilli()),
                            result.getPostTimeDuration());
                    assertEquals(postEntity.getUser().getProfilePicture(), result.getUserProfilePicture());
                }
        );

    }

    // TODO: map post entity to dto with image files
}