package com.jd.twitterclonebackend.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String name;
    private String username;
    private String createdAt;
    private long tweetNo;
    private long followingNo;
    private long followerNo;
    private long notificationNo;
    private byte[] userProfilePicture;
    private byte[] userBackgroundPicture;
    private String description;
    private List<FollowerResponseDto> followers;
    private List<FollowerResponseDto> following;

}
