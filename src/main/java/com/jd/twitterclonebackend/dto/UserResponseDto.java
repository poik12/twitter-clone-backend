package com.jd.twitterclonebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;

    private String name;

    private String username;

    private String createdAt;

    private long tweetsNo;

    private long followingNo;

    private long followersNo;

    private byte[] userProfilePicture;

    private byte[] userBackgroundPicture;

    private String description;

}
