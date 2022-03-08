package com.jd.twitterclonebackend.mapper.mapstruct;

import com.jd.twitterclonebackend.dto.FollowerDto;
import com.jd.twitterclonebackend.dto.UserRequestDto;
import com.jd.twitterclonebackend.dto.UserResponseDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.repository.FollowerRepository;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.service.impl.PostServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class NewUserMapper {

    @Autowired
    protected FileService fileService;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    // todo: add post and comments list in dto
    @Mapping(target = "createdAt", expression = "java(getDateOfCreation(userEntity.getCreatedAt()))")
    @Mapping(target = "tweetNo", expression = "java(userEntity.getPosts().size())")
    @Mapping(target = "followingNo", expression = "java(userEntity.getFollowing().size())")
    @Mapping(target = "followerNo", expression = "java(userEntity.getFollowers().size())")
    @Mapping(target = "followers", expression = "java(getFollowerDtoList(userEntity))")
    @Mapping(target = "following", expression = "java(getFollowingDtoList(userEntity))")
    public abstract UserResponseDto mapFromEntityToUserDto(UserEntity userEntity);

    @NotNull
    protected List<FollowerDto> getFollowingDtoList(UserEntity userEntity) {
        return userEntity.getFollowing()
                .stream()
                .map(followingEntity -> mapFromEntityToFollowerDto(followingEntity.getTo()))
                .collect(Collectors.toList());
    }

    @NotNull
    protected List<FollowerDto> getFollowerDtoList(UserEntity userEntity) {
        return userEntity.getFollowers()
                .stream()
                .map(followerEntity -> mapFromEntityToFollowerDto(followerEntity.getFrom()))
                .collect(Collectors.toList());
    }

    protected String getDateOfCreation(Date createdAt) {
        // Get month and year from Instant created at
        String month = createdAt.toInstant().atZone(ZoneOffset.UTC).getMonth().toString().toLowerCase();
        int year = createdAt.toInstant().atZone(ZoneOffset.UTC).getYear();
        // Return string eg. "August 2014"
        return month + " " + year;
    }

    // todo: check this method
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

    @Mapping(target = "tweetNo", expression = "java(userEntity.getPosts().size())")
    @Mapping(target = "followingNo", expression = "java(userEntity.getFollowing().size())")
    @Mapping(target = "followerNo", expression = "java(userEntity.getFollowers().size())")
    public abstract FollowerDto mapFromEntityToFollowerDto(UserEntity userEntity);

}
