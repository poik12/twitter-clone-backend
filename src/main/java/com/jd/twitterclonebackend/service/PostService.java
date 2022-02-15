package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.PostRequest;
import com.jd.twitterclonebackend.dto.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    // Add new post
//    void addPost(PostRequest postRequest);

    // Get all posts
    List<PostResponse> getAllPosts();

    // Get single post by id
    PostResponse getPostById(Long postId);

    // Get posts by Username
    List<PostResponse> getPostsByUsername(String username);

    //TODO: Get Post by Hashtag list?

    // Add new post
    void addPost(MultipartFile file, PostRequest postRequest);

    void deletePostById(Long postId);
}
