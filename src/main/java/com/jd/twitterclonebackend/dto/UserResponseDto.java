package com.jd.twitterclonebackend.dto;

import lombok.*;

import java.util.List;

@Value
@Builder
public class UserResponseDto {

    Long id;
    String name;
    String username;
    String createdAt;
    long tweetNo;
    long followingNo;
    long followerNo;
    byte[] userProfilePicture;
    byte[] userBackgroundPicture;
    String description;
    List<FollowerDto> followers;
    List<FollowerDto> following;

}
