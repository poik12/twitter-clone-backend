package com.jd.twitterclonebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepliedPostResponseDto {

    private PostResponseDto post;
    private List<CommentResponseDto> comments;

}
