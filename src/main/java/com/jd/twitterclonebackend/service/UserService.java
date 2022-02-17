package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.dto.UserRequestDto;
import com.jd.twitterclonebackend.dto.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    // Get user details by username
    UserResponseDto getUserByUsername(String username);

    List<UserResponseDto> getUsers();

    // Update user details
    void updateUserByUsername(String username,
                              UserRequestDto userRequestDto,
                              MultipartFile profileImageFile,
                              MultipartFile backgroundImageFile);

    void followUser(String username);

    void unfollowUser(String username);

    List<UserResponseDto> getAllFollowers(String username);

    List<UserResponseDto> getAllFollowings(String username);

    boolean checkIfUserIsFollowed(String username, String followed);
}
