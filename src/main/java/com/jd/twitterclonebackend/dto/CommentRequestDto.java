package com.jd.twitterclonebackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    private String username;
    private Long postId;
    private String text;

}
