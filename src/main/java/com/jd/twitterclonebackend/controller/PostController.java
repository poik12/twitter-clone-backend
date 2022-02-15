package com.jd.twitterclonebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.dto.PostRequest;
import com.jd.twitterclonebackend.dto.PostResponse;
import com.jd.twitterclonebackend.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postService;

    // Add new post
    @PostMapping("/add")
    public void addPost(@RequestParam(required = false, value = "file") MultipartFile file,
                        @RequestParam(required = true, value = "postRequest") String postRequest) throws JsonProcessingException {

        PostRequest postRequestMappedFromStringToDto  = new ObjectMapper().readValue(
                postRequest,
                PostRequest.class
        );

        // TODO: change post signature
        if (Objects.nonNull(file)) {
            postService.addPost(
                    file,
                    postRequestMappedFromStringToDto
            );
        }
    }

    // Get all posts sorted by timestamp desc
    @GetMapping("/all")
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    // Get single post by id
    @GetMapping("/{postId}")
    public PostResponse getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    // Get posts by Username
    @GetMapping("/by-user/{username}")
    public List<PostResponse> getPostsByUsername(@PathVariable String username) {
        return postService.getPostsByUsername(username);
    }

    // Delete post by id
    @DeleteMapping("/delete/{postId}")
    public void deletePostById(@PathVariable Long postId) {
        postService.deletePostById(postId);
    }

}
