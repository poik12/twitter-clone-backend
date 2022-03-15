package com.jd.twitterclonebackend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.common.collect.Multimap;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.request.PostRequestDto;
import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final JsonMapper jsonMapper;

    public PostEntity mapFromDtoToEntity(String postRequestJson, UserEntity userEntity) {
        // Map Post request from Json to Dto
        PostRequestDto postRequestDto = jsonMapper.mapFromJsonToDto(
                postRequestJson,
                PostRequestDto.class
        );

        if (Objects.isNull(postRequestJson)) {
            return null;
        }

        return PostEntity.builder()
                .description(postRequestDto.getDescription())
                .user(userEntity)
                .comments(Collections.emptyList())
                .commentNo(0L)
                .build();
    }

    public PostResponseDto mapFromEntityToDto(PostEntity postEntity) {

        if (Objects.isNull(postEntity)) {
            return null;
        }

        return PostResponseDto.builder()
                .id(postEntity.getId())
                .description(postEntity.getDescription())
                .createdAt(postEntity.getCreatedAt())
                .commentNo(getCommentCount(postEntity))
                .likeNo(getPostCount(postEntity))
                .postTimeDuration(getPostTimeDuration(postEntity))
                .username(postEntity.getUser().getUsername())
                .name(postEntity.getUser().getName())
                .userProfilePicture(postEntity.getUser().getProfilePicture())
                .likedByLoggedUser(false)
                .fileContent(Collections.emptyList())
                .build();
    }

    // Number of comments
    private Long getCommentCount(PostEntity postEntity) {
        return Long.parseLong(String.valueOf(postEntity.getComments().size()));
    }

    // Number of likes
    private Long getPostCount(PostEntity postEntity) {
        return Long.parseLong(String.valueOf(postEntity.getUserLikes().size()));
    }

    // Creation time of post
    String getPostTimeDuration(PostEntity postEntity) {
        return TimeAgo.using(postEntity.getCreatedAt().toInstant().toEpochMilli());
    }

}
