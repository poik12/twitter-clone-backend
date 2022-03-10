package com.jd.twitterclonebackend.dto.response;

import lombok.*;

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

    private long tweetNo;
    private long followerNo;
    private long followingNo;

}
