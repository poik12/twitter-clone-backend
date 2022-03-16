package com.jd.twitterclonebackend.dto.response;

import lombok.*;

import javax.persistence.Lob;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowerResponseDto {

    private Long id;
    private String name;
    private String username;
    private byte[] userProfilePicture;
    private long tweetNo;

}
