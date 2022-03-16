package com.jd.twitterclonebackend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import com.jd.twitterclonebackend.dto.response.RepliedPostResponseDto;
import com.jd.twitterclonebackend.entity.HashtagEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.request.PostRequestDto;
import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import com.jd.twitterclonebackend.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final JsonMapper jsonMapper;
    private final HashtagService hashtagService;

    public PostEntity mapFromDtoToEntity(String postRequestJson, UserEntity userEntity) {
        // Map Post request from Json to Dto
        PostRequestDto postRequestDto = jsonMapper.mapFromJsonToDto(
                postRequestJson,
                PostRequestDto.class
        );

        if (Objects.isNull(postRequestJson)) {
            return null;
        }

        // Check if post contains hashtags
        List<HashtagEntity> hashtagEntityList = hashtagService.checkHashTags(postRequestDto.getDescription());

        return PostEntity.builder()
                .description(postRequestDto.getDescription())
                .user(userEntity)
                .comments(Collections.emptyList())
                .commentNo(0L)
                .hashtags(hashtagEntityList)
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

    public RepliedPostResponseDto mapToRepliedPostResponse(PostResponseDto postResponseDto,
                                                           List<CommentResponseDto> commentResponseDtoList) {

        if (Objects.isNull(postResponseDto)
                || Objects.isNull(commentResponseDtoList)) {
            return null;
        }

        return RepliedPostResponseDto.builder()
                .post(postResponseDto)
                .comments(commentResponseDtoList)
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
