package com.jd.twitterclonebackend.dto;

import lombok.*;

import java.time.Instant;
import java.util.Date;

@Value
@Builder
public class PostResponseDto {

    Long id;
    String name;
    String username;
    String description;
    Integer commentNo;
    Date createdAt;
    String postTimeDuration;
    byte[] fileContent;
    byte[] userProfilePicture;
}
