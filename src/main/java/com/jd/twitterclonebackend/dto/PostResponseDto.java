package com.jd.twitterclonebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    private Long id;

    private String description;

    private String url;

    private Instant createdAt;

    private String username;

    private String name;

    private Integer commentCount;

    private String postTimeDuration;

    private byte[] fileContent;

    private byte[] userProfilePicture;
}