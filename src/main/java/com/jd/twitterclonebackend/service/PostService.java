package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.PostRequestDto;
import com.jd.twitterclonebackend.dto.PostResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    // Add new post
    void addPost(MultipartFile file, PostRequestDto postRequestDto);
    // Get all posts
    List<PostResponseDto> getAllPosts();
    // Get single post by id
    PostResponseDto getPostById(Long postId);
    // Get posts by Username
    List<PostResponseDto> getPostsByUsername(String username);
    // Delete post by id
    void deletePostById(Long postId);
}
