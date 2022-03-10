package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;

import java.util.List;

public interface CommentService {

    void addComment(CommentRequestDto commentRequestDto);

    List<CommentResponseDto> getAllCommentsForPost(Long postId);

    List<CommentResponseDto> getAllCommentsForUser(String username);

    void deleteCommentById(Long commentId);
}
