package com.jd.twitterclonebackend.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    private Long tweetId;
    private String username;
    private String name;
    private byte[] profileImage;
    private String timeOfCreation;
    private String text;

}
