package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.dto.response.FollowerResponseDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FollowerMapper {

    public FollowerResponseDto mapFromEntityToDto(UserEntity userEntity) {

        if (Objects.isNull(userEntity)) {
            return null;
        }

        return FollowerResponseDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .userProfilePicture(userEntity.getProfilePicture())
                .tweetNo(userEntity.getTweets().size())
                .build();
    }

    public long getUserFollowerNo(UserEntity userEntity) {
        return userEntity.getFollowers().size();
    }

    public long getUserFollowingNo(UserEntity userEntity) {
        return userEntity.getFollowing().size();
    }

}
