package com.jd.twitterclonebackend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import com.jd.twitterclonebackend.dto.response.RepliedTweetResponseDto;
import com.jd.twitterclonebackend.entity.HashtagEntity;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.request.TweetRequestDto;
import com.jd.twitterclonebackend.dto.response.TweetResponseDto;
import com.jd.twitterclonebackend.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TweetMapper {

    private final JsonMapper jsonMapper;
    private final HashtagService hashtagService;

    public TweetEntity mapFromDtoToEntity(String tweetRequestJson, UserEntity userEntity) {
        // Map Post request from Json to Dto
        TweetRequestDto tweetRequestDto = jsonMapper.mapFromJsonToDto(
                tweetRequestJson,
                TweetRequestDto.class
        );

        if (Objects.isNull(tweetRequestJson)) {
            return null;
        }

        // Check if post contains hashtags
        List<HashtagEntity> hashtagEntityList = hashtagService.checkHashTags(tweetRequestDto.getDescription());

        return TweetEntity.builder()
                .description(tweetRequestDto.getDescription())
                .user(userEntity)
                .comments(Collections.emptyList())
                .commentNo(0L)
                .hashtags(hashtagEntityList)
                .build();
    }

    public TweetResponseDto mapFromEntityToDto(TweetEntity tweetEntity) {

        if (Objects.isNull(tweetEntity)) {
            return null;
        }

        return TweetResponseDto.builder()
                .id(tweetEntity.getId())
                .description(tweetEntity.getDescription())
                .createdAt(tweetEntity.getCreatedAt())
                .commentNo(getCommentCount(tweetEntity))
                .likeNo(getPostCount(tweetEntity))
                .tweetTimeDuration(getPostTimeDuration(tweetEntity))
                .username(tweetEntity.getUser().getUsername())
                .name(tweetEntity.getUser().getName())
                .userProfilePicture(tweetEntity.getUser().getProfilePicture())
                .likedByLoggedUser(false)
                .fileContent(Collections.emptyList())
                .hashtags(tweetEntity.getHashtags()
                        .stream()
                        .map(HashtagEntity::getValue)
                        .collect(Collectors.toList())
                )
                .build();
    }

    public RepliedTweetResponseDto mapToRepliedTweetResponse(TweetResponseDto tweetResponseDto,
                                                             List<CommentResponseDto> commentResponseDtoList) {

        if (Objects.isNull(tweetResponseDto)
                || Objects.isNull(commentResponseDtoList)) {
            return null;
        }

        return RepliedTweetResponseDto.builder()
                .tweet(tweetResponseDto)
                .comments(commentResponseDtoList)
                .build();
    }

    // Number of comments
    private Long getCommentCount(TweetEntity tweetEntity) {
        return Long.parseLong(String.valueOf(tweetEntity.getComments().size()));
    }

    // Number of likes
    private Long getPostCount(TweetEntity tweetEntity) {
        return Long.parseLong(String.valueOf(tweetEntity.getUserLikes().size()));
    }

    // Creation time of post
    String getPostTimeDuration(TweetEntity tweetEntity) {
        return TimeAgo.using(tweetEntity.getCreatedAt().toInstant().toEpochMilli());
    }

}
