package com.jd.twitterclonebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.UserRequest;
import com.jd.twitterclonebackend.dto.UserResponse;
import com.jd.twitterclonebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET USER DETAILS
    @GetMapping("/{username}")
    public UserResponse getUserDetails(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    // UPDATE USER DETAILS
    @PutMapping("/update/{username}")
    public void updateUserDetails(@PathVariable String username,
                                  @RequestParam(required = false, value = "userDetailsRequest") String userDetailsRequest,
                                  @RequestParam(required = false, value = "profileImage")MultipartFile profileImageFile,
                                  @RequestParam(required = false, value = "backgroundImage")MultipartFile backgroundImageFile) throws JsonProcessingException {

        UserRequest userRequest = new ObjectMapper().readValue(
                userDetailsRequest,
                UserRequest.class
        );

        userService.updateUserByUsername(
                username,
                userRequest,
                profileImageFile,
                backgroundImageFile
        );
    }

    // FOLLOW USER BY ITS USERNAME
    @PostMapping("/follow/{username}")
    public void followUser(@PathVariable String username) {
        userService.followUser(username);
    }

    // GET USERS WHO FOLLOW LOGGED USER
    @GetMapping("/all-followers")
    public List<UserResponse> getAllFollowers() {
        return userService.getAllFollowers();
    }

    // GET USERS WHO ARE FOLLOWING BY LOGGED USER
    @GetMapping("/all-following")
    public List<UserResponse> getAllFollowings() {
        return userService.getAllFollowings();
    }


    // TODO: pagination etc.
    @GetMapping("/all")
    public ResponseEntity<List<UserEntity>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

}
