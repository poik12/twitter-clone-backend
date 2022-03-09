package com.jd.twitterclonebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.config.swagger.ApiRestController;
import com.jd.twitterclonebackend.dto.UserRequestDto;
import com.jd.twitterclonebackend.dto.UserResponseDto;
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
    @GetMapping(value ="/{username}")
    public ResponseEntity<UserResponseDto> getUserDetails(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserByUsername(username));
    }

    // GET ALL USERS
    // TODO: pagination etc. some nested exception
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    // UPDATE USER DETAILS
    @PutMapping(value ="/{username}")
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

    // CHECK IF USER IS FOLLOWED BY USERNAME
    @GetMapping(value ="{from}/{to}")
    public boolean checkIfUserIfFollowed(@PathVariable String from, @PathVariable String to) {
        return userService.checkIfUserIsFollowed(from, to);
    }

    // FOLLOW USER BY ITS USERNAME
    @PostMapping(value ="/follow")
    public void followUser(@RequestBody String username) {
        userService.followUser(username);
    }

    // UNFOLLOW USER BY ITS USERNAME
    @PostMapping(value ="/unfollow")
    public void unfollowUser(@RequestBody String username) { userService.unfollowUser(username); }

}
