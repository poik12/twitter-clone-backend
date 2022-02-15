package com.jd.twitterclonebackend.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.domain.PostEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.PostRequest;
import com.jd.twitterclonebackend.dto.PostResponse;
import com.jd.twitterclonebackend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final CommentRepository commentRepository;

    public PostEntity mapFromDtoToEntity(PostRequest postRequest, UserEntity userEntity) {

        if (Objects.isNull(postRequest)) {
            return null;
        }

        return PostEntity.builder()
                .description(postRequest.getDescription())
//                .url(postRequest.getUrl())
                .createdAt(Instant.now())
                .user(userEntity)
                .build();
    }

    public PostResponse mapFromEntityToDto(PostEntity postEntity) {

        if (Objects.isNull(postEntity)) {
            return null;
        }

        return PostResponse.builder()
                .id(postEntity.getId())
                .description(postEntity.getDescription())
                .url(postEntity.getUrl())
                .createdAt(postEntity.getCreatedAt())
//                .commentCount(getCommentCount(postEntity))
                .postTimeDuration(getPostTimeDuration(postEntity))
                .username(postEntity.getUser().getUsername())
                .name(postEntity.getUser().getName())
                .build();
    }

    // TODO: OVERLOADED METHOD
    public PostResponse mapFromEntityToDto(PostEntity postEntity, Map<Long, byte[]> imageFileMap) {

        if (Objects.isNull(postEntity)) {
            return null;
        }

        return PostResponse.builder()
                .id(postEntity.getId())
                .description(postEntity.getDescription())
                .url(postEntity.getUrl())
                .createdAt(postEntity.getCreatedAt())
//                .commentCount(getCommentCount(postEntity))
                .postTimeDuration(getPostTimeDuration(postEntity))
                .username(postEntity.getUser().getUsername())
                .name(postEntity.getUser().getName())
                .fileContent(
                        getImageFileByPostId(
                                postEntity.getId(),
                                imageFileMap
                        ))
                .userProfilePicture(postEntity.getUser().getUserProfilePicture())
                .build();
    }

    // Get image file content in byte[] by post id
    private byte[] getImageFileByPostId(Long postId, Map<Long, byte[]> imageFileMap) {
        return imageFileMap.getOrDefault(postId, null);
    }

    // Number of comments
    private Integer getCommentCount(PostEntity postEntity) {
        return commentRepository.findAllByPost(postEntity).size();
    }

    // Creation time of post
    String getPostTimeDuration(PostEntity postEntity) {
        return TimeAgo.using(postEntity.getCreatedAt().toEpochMilli());
    }

}
