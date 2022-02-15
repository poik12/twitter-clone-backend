package com.jd.twitterclonebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {

    private String username;

    private String name;

    private byte[] profileImage;

    private Long postId;

//    private Instant createdAt;

    private String timeOfCreation;

    private String text;

}
