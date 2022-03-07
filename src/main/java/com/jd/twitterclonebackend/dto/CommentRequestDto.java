package com.jd.twitterclonebackend.dto;

import lombok.*;

@Value
@Builder
public class CommentRequestDto {

    String username;
    Long postId;
    String text;

}
