package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.dto.CommentRequestDto;
import com.jd.twitterclonebackend.dto.CommentResponseDto;
import com.jd.twitterclonebackend.service.impl.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        commentService.createComment(commentRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // GET ALL COMMENT FOR POST SORTED BY TIMESTAMP DESC
    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentResponseDto>> getAllCommentsForUser(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.getAllCommentsForUser(username));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long commentId) {
        commentService.deleteCommentById(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
