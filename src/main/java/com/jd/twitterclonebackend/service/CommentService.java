package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.CommentRequest;
import com.jd.twitterclonebackend.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    void createComment(CommentRequest commentRequest);

    List<CommentResponse> getAllCommentsForPost(Long postId);

    List<CommentResponse> getAllCommentsForUser(String username);

}
