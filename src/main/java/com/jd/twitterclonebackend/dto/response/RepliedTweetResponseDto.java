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
public class RepliedTweetResponseDto {

    private TweetResponseDto tweet;
    private List<CommentResponseDto> comments;

}
