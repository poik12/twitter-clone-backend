package com.jd.twitterclonebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.PostRequestDto;
import com.jd.twitterclonebackend.dto.PostResponseDto;
import com.jd.twitterclonebackend.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/posts")
@RequiredArgsConstructor
@ApiRestController
public class PostController {

    private final PostServiceImpl postService;

    // ADD NEW POST
    @PostMapping
    public void addPost(
            @RequestParam(required = false, value = "file") MultipartFile file,
            @RequestParam(required = true, value = "postRequest") String postRequest
    ) throws JsonProcessingException {

        // Map Post from string to postDTO
        PostRequestDto postRequestDtoMappedFromString = new ObjectMapper().readValue(
                postRequest,
                PostRequestDto.class
        );

        // TODO: change post signature
        if (Objects.nonNull(file)) {
            postService.addPost(
                    file,
                    postRequestDtoMappedFromString
            );
        }
    }

    // GET ALL POSTS SORTED BY TIMESTAMP DESC
    @GetMapping
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    // GET SINGLE POST BY ID
    @GetMapping("/{postId}")
    public PostResponseDto getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    // GET POSTS BY USERNAME
    @GetMapping("/by-user/{username}")
    public List<PostResponseDto> getPostsByUsername(@PathVariable String username) {
        return postService.getPostsByUsername(username);
    }

    // DELETE POST BY ID
    @DeleteMapping("/{postId}")
    public void deletePostById(@PathVariable Long postId) {
        postService.deletePostById(postId);
    }

}
