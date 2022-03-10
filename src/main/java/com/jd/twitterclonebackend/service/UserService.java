package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.response.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    // Get user details by username
    UserResponseDto getUserByUsername(String username);

    List<UserResponseDto> getUsers();
    // Update user details
    void updateUserDetails(String userDetailsRequestJson,
                           MultipartFile profileImageFile,
                           MultipartFile backgroundImageFile);

    void followUser(String username);

    void unfollowUser(String username);

    boolean checkIfUserIsFollowed(String username, String followed);
}
