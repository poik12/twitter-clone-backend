package com.jd.twitterclonebackend.controller;

import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.response.UserResponseDto;
import com.jd.twitterclonebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@ApiRestController
public class UserController {

    private final UserService userService;

    // GET USER DETAILS
    @GetMapping(path = "/{username}")
    public ResponseEntity<UserResponseDto> getUserDetails(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserByUsername(username));
    }

    // GET ALL USERS
    // TODO: pagination etc. some nested exception
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUsers());
    }

    // UPDATE USER DETAILS
    @PutMapping
    public ResponseEntity<Void> updateUserDetails(
            @RequestParam(required = false, value = "userDetailsRequest") String userDetailsRequestJson,
            @RequestParam(required = false, value = "profileImage") MultipartFile profileImageFile,
            @RequestParam(required = false, value = "backgroundImage") MultipartFile backgroundImageFile) {

        userService.updateUserDetails(
                userDetailsRequestJson,
                profileImageFile,
                backgroundImageFile
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // CHECK IF USER IS FOLLOWED BY USERNAME
    @GetMapping(path = "/{from}/{to}")
    public ResponseEntity<Boolean> checkIfUserIsFollowed(@PathVariable String from,
                                                         @PathVariable String to) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.checkIfUserIsFollowed(from, to));
    }

    // FOLLOW USER BY ITS USERNAME
    @PostMapping(path = "/follow")
    public ResponseEntity<Void> followUser(@RequestParam(value = "username") String username) {
        userService.followUser(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // UNFOLLOW USER BY ITS USERNAME
    @PostMapping(path = "/unfollow")
    public ResponseEntity<Void> unfollowUser(@RequestParam(value = "username") String username) {
        userService.unfollowUser(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
