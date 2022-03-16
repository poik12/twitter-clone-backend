package com.jd.twitterclonebackend.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    private String username;
    private Long tweetId;
    private String text;

}
