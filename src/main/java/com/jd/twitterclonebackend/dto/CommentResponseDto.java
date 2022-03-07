package com.jd.twitterclonebackend.dto;

import lombok.*;

@Value
@Builder
public class CommentResponseDto {

    String username;
    String name;
    byte[] profileImage;
    Long postId;
    String timeOfCreation;
    String text;

}
