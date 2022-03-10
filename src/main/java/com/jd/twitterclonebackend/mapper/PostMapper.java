package com.jd.twitterclonebackend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.request.PostRequestDto;
import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

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

//    public PostResponseDto mapFromEntityToDto(PostEntity postEntity) {
//
//        if (Objects.isNull(postEntity)) {
//            return null;
//        }
//
//        return PostResponseDto.builder()
//                .id(postEntity.getId())
//                .description(postEntity.getDescription())
//                .createdAt(postEntity.getCreatedAt())
//                .commentNo(getCommentCount(postEntity))
//                .postTimeDuration(getPostTimeDuration(postEntity))
//                .username(postEntity.getUser().getUsername())
//                .name(postEntity.getUser().getName())
//                .build();
//    }

    // TODO: OVERLOADED METHOD
    public PostResponseDto mapFromEntityToDto(PostEntity postEntity, Map<Long, byte[]> imageFileMap) {

        if (Objects.isNull(postEntity)) {
            return null;
        }

        return PostResponseDto.builder()
                .id(postEntity.getId())
                .description(postEntity.getDescription())
                .createdAt(postEntity.getCreatedAt())
                .commentNo(getCommentCount(postEntity))
                .postTimeDuration(getPostTimeDuration(postEntity))
                .username(postEntity.getUser().getUsername())
                .name(postEntity.getUser().getName())
                .fileContent(getImageFileByPostId(postEntity.getId(), imageFileMap))
                .userProfilePicture(postEntity.getUser().getProfilePicture())
                .build();
        
    }

    // Get image file content in byte[] by post id
    private byte[] getImageFileByPostId(Long postId, Map<Long, byte[]> imageFileMap) {
        return imageFileMap.getOrDefault(postId, null);
    }

    // Number of comments
    private Long getCommentCount(PostEntity postEntity) {
//        return commentRepository.findAllByPost(postEntity).size();
        return (long) postEntity.getComments().size();
    }

    // Creation time of post
    String getPostTimeDuration(PostEntity postEntity) {
        return TimeAgo.using(postEntity.getCreatedAt().toInstant().toEpochMilli());
    }

}
