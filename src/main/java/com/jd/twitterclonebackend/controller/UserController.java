package com.jd.twitterclonebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.UserRequestDto;
import com.jd.twitterclonebackend.dto.UserResponseDto;
import com.jd.twitterclonebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api-version}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET USER DETAILS
    @GetMapping("/{username}")
    public UserResponseDto getUserDetails(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    // GET ALL USERS
    // TODO: pagination etc. some nested exception
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    // UPDATE USER DETAILS
    @PutMapping("/{username}")
    public void updateUserDetails(
            @PathVariable String username,
            @RequestParam(required = false, value = "userDetailsRequest") String userDetailsRequest,
            @RequestParam(required = false, value = "profileImage") MultipartFile profileImageFile,
            @RequestParam(required = false, value = "backgroundImage") MultipartFile backgroundImageFile
    ) throws JsonProcessingException {

        UserRequestDto userRequestDto = new ObjectMapper().readValue(
                userDetailsRequest,
                UserRequestDto.class
        );

        userService.updateUserByUsername(
                username,
                userRequestDto,
                profileImageFile,
                backgroundImageFile
        );
    }

    // FOLLOW USER BY ITS USERNAME
    @PostMapping("/{username}/follow")
    public void followUser(@PathVariable String username) {
        userService.followUser(username);
    }

    // UNFOLLOW USER BY ITS USERNAME
    @PostMapping("/{username}/unfollow")
    public void unfollowUser(@PathVariable String username) { userService.unfollowUser(username); }
    // GET USERS WHO FOLLOW LOGGED USER
    @GetMapping("/all-followers")
    public List<UserResponseDto> getAllFollowers() {
        return userService.getAllFollowers();
    }

    // GET USERS WHO ARE FOLLOWING BY LOGGED USER
    @GetMapping("/all-following")
    public List<UserResponseDto> getAllFollowings() {
        return userService.getAllFollowings();
    }

}
