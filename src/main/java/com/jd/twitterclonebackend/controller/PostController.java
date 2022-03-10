package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import com.jd.twitterclonebackend.service.PostService;
import com.jd.twitterclonebackend.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
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
    public void addPost(
            @RequestParam(required = false, value = "file") MultipartFile file,
            @RequestParam(required = true, value = "postRequest") String postRequestJson
    ) {
        postService.addPost(file, postRequestJson);
    }

    // GET ALL POSTS SORTED BY TIMESTAMP DESC
    @GetMapping
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    // GET SINGLE POST BY ID
    @GetMapping(path = "/{postId}")
    public PostResponseDto getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    // GET POSTS BY USERNAME
    @GetMapping(path = "/by-user/{username}")
    public List<PostResponseDto> getPostsByUsername(@PathVariable String username) {
        return postService.getPostsByUsername(username);
    }


    // DELETE POST BY ID
    @DeleteMapping(path = "/{postId}")
    public void deletePostById(@PathVariable Long postId) {
        postService.deletePostById(postId);
    }

}
