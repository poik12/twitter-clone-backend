package com.jd.twitterclonebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    private Long id;

    private String name;

    private String username;

    private String description;

    private Integer commentNo;

    private Date createdAt;

    private String postTimeDuration;

    private byte[] fileContent;

    private byte[] userProfilePicture;
}
