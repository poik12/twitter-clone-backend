package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.CommentRequestDto;
import com.jd.twitterclonebackend.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {

    void createComment(CommentRequestDto commentRequestDto);

    List<CommentResponseDto> getAllCommentsForPost(Long postId);

    List<CommentResponseDto> getAllCommentsForUser(String username);

    void deleteCommentById(Long commentId);
}
