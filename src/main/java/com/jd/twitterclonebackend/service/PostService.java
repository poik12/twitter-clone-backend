package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    void addPost(MultipartFile[] file, String postRequestJson);

    List<PostResponseDto> getAllPosts(Pageable pageable);

    PostResponseDto getPostById(Long postId);

    List<PostResponseDto> getPostsByUsername(String username, Pageable pageable);

    void deletePostById(Long postId);

    void likePostById(Long postId);

    List<PostResponseDto> getLikedPostsByUsername(String username, Pageable pageable);

}
