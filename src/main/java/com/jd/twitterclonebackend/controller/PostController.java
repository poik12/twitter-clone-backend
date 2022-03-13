package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import com.jd.twitterclonebackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // GET ALL POSTS IN PAGES SORTED BY TIMESTAMP DESC
    @GetMapping()
    public ResponseEntity<List<PostResponseDto>> getAllPostsPageable(@RequestParam("pageNumber") int pageNumber,
                                                                     @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.DESC,
                "createdAt"
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getAllPosts(pageable));

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
    public ResponseEntity<List<PostResponseDto>> getPostsByUsername(@PathVariable String username,
                                                                    @RequestParam("pageNumber") int pageNumber,
                                                                    @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.DESC,
                "createdAt"
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getPostsByUsername(username, pageable));
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

        // todo: send notification to post creator
    }

    // GET LIKED POSTS
    @GetMapping(path = "/like/by-user/{username}")
    public ResponseEntity<List<PostResponseDto>> getLikedPostsByUsername(@PathVariable String username,
                                                                         @RequestParam("pageNumber") int pageNumber,
                                                                         @RequestParam("pageSize") int pageSize) {
        // todo: add column creatAt to user_post_list and and search by it
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.Direction.DESC,
                "createdAt"
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.getLikedPostsByUsername(username, pageable));
    }

}
