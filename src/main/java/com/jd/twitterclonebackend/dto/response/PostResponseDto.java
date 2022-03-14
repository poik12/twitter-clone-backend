package com.jd.twitterclonebackend.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String name;
    private String username;
    private String description;
    private Long commentNo;
    private Long likeNo;
    private Date createdAt;
    private String postTimeDuration;
    private List<byte[]> fileContent;
    private byte[] userProfilePicture;
    private boolean likedByLoggedUser;

}
