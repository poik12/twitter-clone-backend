package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    // Add new post
    void addPost(MultipartFile file, String postRequestJson);
    // Get all posts
    List<PostResponseDto> getAllPosts();
    // Get single post by id
    PostResponseDto getPostById(Long postId);
    // Get posts by Username
    List<PostResponseDto> getPostsByUsername(String username);
    // Delete post by id
    void deletePostById(Long postId);
    // Like post by id
    void likePostById(Long postId);

    List<PostResponseDto> getLikedPosts();
}
