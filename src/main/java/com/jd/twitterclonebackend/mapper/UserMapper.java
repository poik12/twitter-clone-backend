package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.dto.response.FollowerDto;
import com.jd.twitterclonebackend.dto.request.UserDetailsRequestDto;
import com.jd.twitterclonebackend.dto.response.UserResponseDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.service.PostService;
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

    private final PostService postService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;
    private final JsonMapper jsonMapper;

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
                                             String userDetailsRequestJson,
                                             MultipartFile profileImageFile,
                                             MultipartFile backgroundImageFile) {

        // TODO: Description not added yet
        // TODO: Add username line in frontend


        // Map User Details from Json to Dto
        UserDetailsRequestDto userDetailsRequestDto = jsonMapper.mapFromJsonToDto(
                userDetailsRequestJson,
                UserDetailsRequestDto.class
        );

        if (!userDetailsRequestDto.getName().isBlank()) {
            userEntity.setName(userDetailsRequestDto.getName());
        }
        if (!userDetailsRequestDto.getUsername().isBlank()) {
            userEntity.setUsername(userDetailsRequestDto.getUsername());
        }
        if (!userDetailsRequestDto.getEmailAddress().isBlank()) {
            userEntity.setEmailAddress(userDetailsRequestDto.getEmailAddress());
        }
        if (!userDetailsRequestDto.getPhoneNumber().isBlank()) {
            userEntity.setPhoneNumber(userDetailsRequestDto.getPhoneNumber());
        }
        if (!userDetailsRequestDto.getPassword().isBlank()) {
            userEntity.setPassword(passwordEncoder.encode(userDetailsRequestDto.getPassword()));
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
