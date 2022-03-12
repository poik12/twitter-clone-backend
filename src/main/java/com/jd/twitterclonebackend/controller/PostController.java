package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import com.jd.twitterclonebackend.service.PostService;
import com.jd.twitterclonebackend.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/posts")
@RequiredArgsConstructor
@ApiRestController
public class PostController {

    private final PostService postService;

    // ADD NEW POST
    @PostMapping
    public ResponseEntity<Void> addPost(
            @RequestParam(required = false, value = "file") MultipartFile file,
            @RequestParam(required = true, value = "postRequest") String postRequestJson
    ) {
        postService.addPost(file, postRequestJson);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    // GET ALL POSTS SORTED BY TIMESTAMP DESC
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getAllPosts());
    }

    // GET SINGLE POST BY ID
    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getPostById(postId));
    }

    // GET POSTS BY USERNAME
    @GetMapping(path = "/by-user/{username}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUsername(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getPostsByUsername(username));
    }

    // DELETE POST BY ID
    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long postId) {
        postService.deletePostById(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    // LIKE POST
    @GetMapping(path = "/like/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postService.likePostById(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    // GET LIKED POSTS
    @GetMapping(path = "/like")
    public ResponseEntity<List<PostResponseDto>> likePost() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getLikedPosts());
    }

}
