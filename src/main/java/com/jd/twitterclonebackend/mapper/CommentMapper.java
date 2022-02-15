package com.jd.twitterclonebackend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.domain.CommentEntity;
import com.jd.twitterclonebackend.domain.PostEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.CommentRequest;
import com.jd.twitterclonebackend.dto.CommentResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
public class CommentMapper {

    public CommentEntity mapFromDtoToEntity(CommentRequest commentRequest,
                                            PostEntity postEntity,
                                            UserEntity userEntity) {

        if (Objects.isNull(commentRequest)) {
            return null;
        }

        return CommentEntity.builder()
                .post(postEntity)
                .user(userEntity)
                .createdAt(Instant.now())
                .text(commentRequest.getText())
                .build();
    }

    public CommentResponse mapFromEntityToDto(CommentEntity commentEntity) {

        if (Objects.isNull(commentEntity)) {
            return null;
        }

        return CommentResponse.builder()
                .name(commentEntity.getUser().getName())
                .username(commentEntity.getUser().getUsername())
                .profileImage(commentEntity.getUser().getUserProfilePicture())
                .postId(commentEntity.getPost().getId())
                .text(commentEntity.getText())
                .timeOfCreation(getCommentTimeDuration(commentEntity))
                .build();
    }

    // Creation time of comment
    String getCommentTimeDuration(CommentEntity commentEntity) {
        return TimeAgo.using(commentEntity.getCreatedAt().toEpochMilli());
    }

}
