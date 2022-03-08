package com.jd.twitterclonebackend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.CommentRequestDto;
import com.jd.twitterclonebackend.dto.CommentResponseDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommentMapper {

    public CommentEntity mapFromDtoToEntity(CommentRequestDto commentRequestDto,
                                            PostEntity postEntity,
                                            UserEntity userEntity) {

        if (Objects.isNull(commentRequestDto)) {
            return null;
        }

        return CommentEntity.builder()
                .post(postEntity)
                .user(userEntity)
                .text(commentRequestDto.getText())
                .build();
    }

    public CommentResponseDto mapFromEntityToDto(CommentEntity commentEntity) {

        if (Objects.isNull(commentEntity)) {
            return null;
        }

        return CommentResponseDto.builder()
                .name(commentEntity.getUser().getName())
                .username(commentEntity.getUser().getUsername())
                .profileImage(commentEntity.getUser().getProfilePicture())
                .postId(commentEntity.getPost().getId())
                .text(commentEntity.getText())
                .timeOfCreation(getCommentTimeDuration(commentEntity))
                .build();
    }

    // Creation time of comment passed when it was created
    String getCommentTimeDuration(CommentEntity commentEntity) {
        return TimeAgo.using(commentEntity.getCreatedAt().toEpochMilli());
    }

}
