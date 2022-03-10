package com.jd.twitterclonebackend.mapper.mapstruct;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewCommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", source = "userEntity")
    @Mapping(target = "post", source = "postEntity")
    CommentEntity mapFromDtoToEntity(CommentRequestDto commentRequestDto,
                                     PostEntity postEntity,
                                     UserEntity userEntity);

    @Mapping(target = "username", expression = "java(commentEntity.getUser().getUsername())")
    @Mapping(target = "name", expression = "java(commentEntity.getUser().getName())")
    @Mapping(target = "profileImage", expression = "java(commentEntity.getUser().getProfilePicture())")
    @Mapping(target = "postId", expression = "java(commentEntity.getPost().getId())")
    @Mapping(target = "timeOfCreation", expression = "java(getCommentTimeDuration(commentEntity))")
    CommentResponseDto mapFromEntityToDto(CommentEntity commentEntity);

    // Creation time of comment passed when it was created
    default String getCommentTimeDuration(CommentEntity commentEntity) {
        return TimeAgo.using(commentEntity.getCreatedAt().toEpochMilli());
    }

}
