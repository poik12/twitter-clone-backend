package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.PostResponse;
import com.jd.twitterclonebackend.dto.UserRequest;
import com.jd.twitterclonebackend.dto.UserResponse;
import com.jd.twitterclonebackend.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PostServiceImpl postService;

    public UserResponse mapFromEntityToDto(UserEntity userEntity) {

        if (Objects.isNull(userEntity)) {
            return null;
        }

        return UserResponse.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .createdAt(getDateOfCreation(userEntity.getCreatedAt()))
                .tweetsNo(getUserPosts(userEntity.getUsername()))
                .followingNo(getUserFollowings(userEntity))
                .followersNo(getUserFollowers(userEntity))
                .userProfilePicture(userEntity.getUserProfilePicture())
                .userBackgroundPicture(userEntity.getUserBackgroundPicture())
                .description(userEntity.getDescription())
                .build();
    }

    private String getDateOfCreation(Instant createAt) {
        // Get month and year from Instant created at
        String month = createAt.atZone(ZoneOffset.UTC).getMonth().toString().toLowerCase();
        int year = createAt.atZone(ZoneOffset.UTC).getYear();
        // Return string eg. "August 2014"
        return month + " " + year;
    }

    private long getUserPosts(String username) {
        // Get posts for user and return length of post list
        return postService.getPostsByUsername(username).size();
    }

    private long getUserFollowers(UserEntity userEntity) {
        return userEntity.getFollowers().size();
    }

    private long getUserFollowings(UserEntity userEntity) {
        return userEntity.getFollowing().size();
    }


    public UserEntity mapFromDtoToEntity(UserEntity userEntity,
                                         UserRequest userRequest,
                                         MultipartFile profileImageFile,
                                         MultipartFile backgroundImageFile) {

        if (Objects.isNull(userRequest)) {
            return null;
        }

        if (userRequest.getUsername().equals("")) {
            userRequest.setUsername(userEntity.getUsername());
        }

//        TODO: Description, profile image, background image not added yet
        userEntity.setName(userRequest.getName());
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setEmailAddress(userRequest.getEmailAddress());
        userEntity.setPhoneNumber(userRequest.getPhoneNumber());
        userEntity.setPassword(passwordEncoder().encode(userRequest.getPassword()));

        if (Objects.nonNull(profileImageFile)) {
            try {
                userEntity.setUserProfilePicture(profileImageFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Objects.nonNull(backgroundImageFile)) {
            try {
                userEntity.setUserBackgroundPicture(backgroundImageFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return userEntity;
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
