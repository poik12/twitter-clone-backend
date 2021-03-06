package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    void addComment(CommentRequestDto commentRequestDto);

    List<CommentResponseDto> getAllCommentsForTweet(Long tweetId, Pageable pageable);

    List<CommentResponseDto> getThreeLastCommentsForTweetByUsernameAndTweetId(String username,
                                                                              Long tweetId,
                                                                              Pageable pageable);

    void deleteCommentById(Long commentId);
}
