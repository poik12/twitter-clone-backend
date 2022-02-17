package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.FollowerDto;
import com.jd.twitterclonebackend.dto.UserRequestDto;
import com.jd.twitterclonebackend.dto.UserResponseDto;
import com.jd.twitterclonebackend.repository.FollowerRepository;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.service.impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PostServiceImpl postService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final FollowerRepository followerRepository;

    public UserResponseDto mapFromEntityToUserDto(UserEntity userEntity) {

        if (Objects.isNull(userEntity)) {
            return null;
        }

        return UserResponseDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .createdAt(getDateOfCreation(userEntity.getCreatedAt()))
                .tweetNo(getUserPostsSize(userEntity.getUsername()))
                .followingNo(getUserFollowings(userEntity))
                .followerNo(getUserFollowers(userEntity))
                .userProfilePicture(userEntity.getProfilePicture())
                .userBackgroundPicture(userEntity.getBackgroundPicture())
                .description(userEntity.getDescription())
                .followers(userEntity.getFollowers()
                        .stream()
                        .map(followerEntity -> mapFromEntityToFollowerDto(followerEntity.getFrom()))
                        .collect(Collectors.toList())
                )
                .following(userEntity.getFollowing()
                        .stream()
                        .map(followingEntity -> mapFromEntityToFollowerDto(followingEntity.getTo()))
                        .collect(Collectors.toList())
                )
                .build();
    }

    private String getDateOfCreation(Date createdAt) {
        // Get month and year from Instant created at
        String month = createdAt.toInstant().atZone(ZoneOffset.UTC).getMonth().toString().toLowerCase();
        int year = createdAt.toInstant().atZone(ZoneOffset.UTC).getYear();
        // Return string eg. "August 2014"
        return month + " " + year;
    }

    private long getUserPostsSize(String username) {
        // Get posts for user and return length of post list
        return postService.getPostsByUsername(username).size();
    }

    private long getUserFollowers(UserEntity userEntity) {
        return userEntity.getFollowers().size();
    }

    private long getUserFollowings(UserEntity userEntity) {
        return userEntity.getFollowing().size();
    }


    public UserEntity mapFromUserDtoToEntity(UserEntity userEntity,
                                             UserRequestDto userRequestDto,
                                             MultipartFile profileImageFile,
                                             MultipartFile backgroundImageFile) {

        // TODO: Description not added yet
        // TODO: Add username line in frontend

        if (!userRequestDto.getName().isBlank()) {
            userEntity.setName(userRequestDto.getName());
        }
        if (!userRequestDto.getUsername().isBlank()) {
            userEntity.setUsername(userRequestDto.getUsername());
        }
        if (!userRequestDto.getEmailAddress().isBlank()) {
            userEntity.setEmailAddress(userRequestDto.getUsername());
        }
        if (!userRequestDto.getPhoneNumber().isBlank()) {
            userEntity.setPhoneNumber(userRequestDto.getPhoneNumber());
        }
        if (!userRequestDto.getPassword().isBlank()) {
            userEntity.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }
        if (Objects.nonNull(profileImageFile)) {
            userEntity.setProfilePicture(fileService.convertImageFileToByteArray(profileImageFile));
        }
        if (Objects.nonNull(backgroundImageFile)) {
            userEntity.setBackgroundPicture(fileService.convertImageFileToByteArray(backgroundImageFile));
        }

        return userEntity;
    }


    private FollowerDto mapFromEntityToFollowerDto(UserEntity userEntity) {
        if (Objects.isNull(userEntity)) {
            return null;
        }

        return FollowerDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .emailAddress(userEntity.getEmailAddress())
                .userProfilePicture(userEntity.getProfilePicture())
                .userBackgroundPicture(userEntity.getBackgroundPicture())
                .tweetNo(getUserPostsSize(userEntity.getUsername()))
                .followingNo(getUserFollowings(userEntity))
                .followerNo(getUserFollowers(userEntity))
                .build();

    }
}
