package com.jd.twitterclonebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowerDto {


    private Long id;

    private String name;

    private String username;

    private String emailAddress;

    @Lob
    private byte[] userProfilePicture;

    @Lob
    private byte[] userBackgroundPicture;

    private Long tweetNo;

    private Long followerNo;

    private Long followingNo;

}
