package com.jd.twitterclonebackend.unit.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.dto.CommentRequestDto;
import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.CommentMapper;
import com.jd.twitterclonebackend.unit.InitUnitTestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest extends InitUnitTestData {

    @InjectMocks
    private CommentMapper commentMapper;

    @Test
    void should_mapFromCommentDto_ToCommentEntity() {
        // given
        UserEntity userEntity = initUserEntity();
        PostEntity postEntity = initPostEntity(userEntity);
        CommentRequestDto commentRequestDto = initCommentRequestDto();

        // when
        var result = commentMapper.mapFromDtoToEntity(
                commentRequestDto,
                postEntity,
                userEntity
        );
        System.out.println(result);
        // then
        assertAll(
                () -> {
                    assertEquals(userEntity, result.getUser());
                    assertEquals(postEntity, result.getPost());
                    assertEquals(commentRequestDto.getText(), result.getText());
                }
        );

    }

    @Test
    void should_mapFromCommentEntity_toCommentResponseDto() {
        // given
        UserEntity userEntity = initUserEntity();
        PostEntity postEntity = initPostEntity(userEntity);
        CommentEntity commentEntity = initCommentEntity(userEntity, postEntity);

        // when
        var result = commentMapper.mapFromEntityToDto(commentEntity);

        System.out.println(result);
        // then
        assertAll(
                () -> {
                    assertEquals(commentEntity.getUser().getName(), result.getName());
                    assertEquals(commentEntity.getUser().getUsername(), result.getUsername());
                    assertEquals(commentEntity.getUser().getProfilePicture(), result.getProfileImage());
                    assertEquals(commentEntity.getPost().getId(), result.getPostId());
                    assertEquals(commentEntity.getText(), result.getText());
                    assertEquals(
                            TimeAgo.using(commentEntity.getCreatedAt().toEpochMilli()),
                            result.getTimeOfCreation()
                    );
                }
        );

    }

}