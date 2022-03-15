package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    void addComment(CommentRequestDto commentRequestDto);

    List<CommentResponseDto> getAllCommentsForPost(Long postId, Pageable pageable);

    List<CommentResponseDto> getThreeLastCommentsForPostByUsernameAndPostId(String username,
                                                                            Long postId,
                                                                            Pageable pageable);

    void deleteCommentById(Long commentId);
}
