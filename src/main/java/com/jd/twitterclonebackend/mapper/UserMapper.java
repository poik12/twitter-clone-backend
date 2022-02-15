package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.UserRequestDto;
import com.jd.twitterclonebackend.dto.UserResponseDto;
import com.jd.twitterclonebackend.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PostServiceImpl postService;

    public UserResponseDto mapFromEntityToDto(UserEntity userEntity) {

        if (Objects.isNull(userEntity)) {
            return null;
        }

        return UserResponseDto.builder()
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

    private String getDateOfCreation(Date createdAt) {
        // Get month and year from Instant created at
        String month = createdAt.toInstant().atZone(ZoneOffset.UTC).getMonth().toString().toLowerCase();
        int year = createdAt.toInstant().atZone(ZoneOffset.UTC).getYear();
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
                                         UserRequestDto userRequestDto,
                                         MultipartFile profileImageFile,
                                         MultipartFile backgroundImageFile) {

        if (Objects.isNull(userRequestDto)) {
            return null;
        }

        if (userRequestDto.getUsername().equals("")) {
            userRequestDto.setUsername(userEntity.getUsername());
        }

//        TODO: Description, profile image, background image not added yet
        userEntity.setName(userRequestDto.getName());
        userEntity.setUsername(userRequestDto.getUsername());
        userEntity.setEmailAddress(userRequestDto.getEmailAddress());
        userEntity.setPhoneNumber(userRequestDto.getPhoneNumber());
        userEntity.setPassword(passwordEncoder().encode(userRequestDto.getPassword()));

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
