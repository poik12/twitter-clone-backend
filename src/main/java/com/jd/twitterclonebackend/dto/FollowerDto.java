package com.jd.twitterclonebackend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.persistence.Lob;

@Value
@Builder
public class FollowerDto {


    Long id;
    String name;
    String username;
    String emailAddress;

    @Lob
    byte[] userProfilePicture;
    @Lob
    byte[] userBackgroundPicture;

    Long tweetNo;
    Long followerNo;
    Long followingNo;

}
