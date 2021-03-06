package com.jd.twitterclonebackend.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetResponseDto {

    private Long id;
    private String name;
    private String username;
    private String description;
    private Long commentNo;
    private Long likeNo;
    private Date createdAt;
    private String tweetTimeDuration;
    private List<byte[]> fileContent;
    private byte[] userProfilePicture;
    private boolean likedByLoggedUser;
    private List<String> hashtags;

}
