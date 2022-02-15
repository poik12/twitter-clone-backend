package com.jd.twitterclonebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {

    private String username;

    private String name;

    private byte[] profileImage;

    private Long postId;

//    private Instant createdAt;

    private String timeOfCreation;

    private String text;

}
