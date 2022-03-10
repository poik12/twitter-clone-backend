package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import com.jd.twitterclonebackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/comments")
@RequiredArgsConstructor
@ApiRestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody CommentRequestDto commentRequestDto) {
        commentService.addComment(commentRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    // GET ALL COMMENT FOR POST SORTED BY TIMESTAMP DESC
    @GetMapping(path ="/by-post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping(path ="/by-user/{username}")
    public ResponseEntity<List<CommentResponseDto>> getAllCommentsForUser(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.getAllCommentsForUser(username));
    }

    @DeleteMapping(path ="/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long commentId) {
        commentService.deleteCommentById(commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


}
