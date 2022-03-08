package com.jd.twitterclonebackend.mapper.mapstruct;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.dto.PostRequestDto;
import com.jd.twitterclonebackend.dto.PostResponseDto;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.repository.CommentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class NewPostMapper {

    @Autowired
    protected CommentRepository commentRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "user", source = "userEntity")
    @Mapping(target = "description", expression = "java(postRequestDto.getDescription())")
    public abstract PostEntity mapFromDtoToEntity(PostRequestDto postRequestDto, UserEntity userEntity);

    @Mapping(target = "commentNo", expression = "java(getCommentCount(postEntity))")
    @Mapping(target = "postTimeDuration", expression = "java(getPostTimeDuration(postEntity))")
    @Mapping(target = "username", expression = "java(postEntity.getUser().getUsername())")
    @Mapping(target = "name", expression = "java(postEntity.getUser().getName())")
    @Mapping(target = "fileContent", expression = "java(getImageFileByPostId(postEntity.getId(), imageFileMap))")
    @Mapping(target = "userProfilePicture", expression = "java(postEntity.getUser().getProfilePicture())")
    public abstract PostResponseDto mapFromEntityToDto(PostEntity postEntity, Map<Long, byte[]> imageFileMap);

    // Get image file content in byte[] by post id
    protected byte[] getImageFileByPostId(Long postId, Map<Long, byte[]> imageFileMap) {
        return imageFileMap.getOrDefault(postId, null);
    }

    // Number of comments
    protected Integer getCommentCount(PostEntity postEntity) {
        return postEntity.getComments().size();
    }

    // Creation time of post
    protected String getPostTimeDuration(PostEntity postEntity) {
        return TimeAgo.using(postEntity.getCreatedAt().toInstant().toEpochMilli());
    }
}
