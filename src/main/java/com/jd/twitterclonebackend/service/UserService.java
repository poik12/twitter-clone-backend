package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.UserRequest;
import com.jd.twitterclonebackend.dto.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    // Get user details by username
    UserResponse getUserByUsername(String username);

    List<UserEntity> getUsers();

    // Update user details
    void updateUserByUsername(String username,
                              UserRequest userRequest,
                              MultipartFile profileImageFile,
                              MultipartFile backgroundImageFile);

    void followUser(String username);

    List<UserResponse> getAllFollowers();

    List<UserResponse> getAllFollowings();
}
