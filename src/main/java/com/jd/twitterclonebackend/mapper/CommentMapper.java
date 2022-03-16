package com.jd.twitterclonebackend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommentMapper {

    public CommentEntity mapFromDtoToEntity(CommentRequestDto commentRequestDto,
                                            TweetEntity tweetEntity,
                                            UserEntity userEntity) {

        if (Objects.isNull(commentRequestDto)) {
            return null;
        }

        return CommentEntity.builder()
                .tweet(tweetEntity)
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
                .tweetId(commentEntity.getTweet().getId())
                .text(commentEntity.getText())
                .timeOfCreation(getCommentTimeDuration(commentEntity))
                .build();
    }

    // Creation time of comment passed when it was created
    private String getCommentTimeDuration(CommentEntity commentEntity) {
        return TimeAgo.using(commentEntity.getCreatedAt().toEpochMilli());
    }

}
