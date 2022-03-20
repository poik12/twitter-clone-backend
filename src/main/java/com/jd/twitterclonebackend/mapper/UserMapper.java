package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.dto.request.UserDetailsRequestDto;
import com.jd.twitterclonebackend.dto.response.FollowerResponseDto;
import com.jd.twitterclonebackend.dto.response.UserResponseDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class UserMapper {

    private final FileService fileService;

    private final PasswordEncoder passwordEncoder;

    private final JsonMapper jsonMapper;
    private final FollowerMapper followerMapper;

    public UserResponseDto mapFromEntityToDto(UserEntity userEntity) {

        if (Objects.isNull(userEntity)) {
            return null;
        }

        return UserResponseDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .createdAt(getDateOfCreation(userEntity.getCreatedAt()))
                .tweetNo(userEntity.getTweets().size())
                .followingNo(followerMapper.getUserFollowings(userEntity))
                .followerNo(followerMapper.getUserFollowers(userEntity))
                .notificationNo(userEntity.getPublishedNotifications().size())
                .userProfilePicture(userEntity.getProfilePicture())
                .userBackgroundPicture(userEntity.getBackgroundPicture())
                .description(userEntity.getDescription())
                .followers(getFollowerDtoList(userEntity))
                .following(getFollowingDtoList(userEntity))
                .build();
    }

    public UserEntity mapFromDtoToEntity(UserEntity userEntity,
                                         String userDetailsRequestJson,
                                         MultipartFile profileImageFile,
                                         MultipartFile backgroundImageFile) {

        // Map User Details from Json to Dto
        UserDetailsRequestDto userDetailsRequestDto = jsonMapper.mapFromJsonToDto(
                userDetailsRequestJson,
                UserDetailsRequestDto.class
        );

        if (!userDetailsRequestDto.getName().isBlank()) {
            userEntity.setName(userDetailsRequestDto.getName());
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
        if (!userDetailsRequestDto.getDescription().isBlank()) {
            userEntity.setDescription(userDetailsRequestDto.getDescription());
        }
        if (Objects.nonNull(profileImageFile)) {
            userEntity.setProfilePicture(fileService.convertFileToByteArray(profileImageFile));
        }
        if (Objects.nonNull(backgroundImageFile)) {
            userEntity.setBackgroundPicture(fileService.convertFileToByteArray(backgroundImageFile));
        }
        return userEntity;
    }

    private String getDateOfCreation(Date createdAt) {
        // Get month and year from Instant created at
        String month = createdAt.toInstant().atZone(ZoneOffset.UTC).getMonth().toString().toLowerCase();
        int year = createdAt.toInstant().atZone(ZoneOffset.UTC).getYear();
        // Return string eg. "August 2014"
        return month + " " + year;
    }

    private List<FollowerResponseDto> getFollowerDtoList(UserEntity userEntity) {
        return userEntity.getFollowers()
                .stream()
                .map(followerEntity -> followerMapper.mapFromEntityToDto(followerEntity.getFrom()))
                .collect(Collectors.toList());
    }

    private List<FollowerResponseDto> getFollowingDtoList(UserEntity userEntity) {
        return userEntity.getFollowing()
                .stream()
                .map(followingEntity ->  followerMapper.mapFromEntityToDto(followingEntity.getTo()))
                .collect(Collectors.toList());
    }
}
