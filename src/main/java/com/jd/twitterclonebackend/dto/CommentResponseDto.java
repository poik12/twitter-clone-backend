package com.jd.twitterclonebackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    private String username;
    private String name;
    private byte[] profileImage;
    private Long postId;
    private String timeOfCreation;
    private String text;

}
