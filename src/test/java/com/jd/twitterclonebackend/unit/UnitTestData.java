package com.jd.twitterclonebackend.unit;

import com.jd.twitterclonebackend.dto.RegisterRequestDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.enums.UserRole;
import com.jd.twitterclonebackend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public abstract class UnitTestData {

    @Autowired
    protected FileService fileService;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected static final String DEFAULT_PROFILE_PICTURE_PATH = "src/main/resources/images/default_profile_picture_twitter.png";
    protected static final String DEFAULT_BACKGROUND_PICTURE_PATH = "src/main/resources/images/default_background_picture_twitter.png";


    protected static final String USER_PRIME_NAME = "Test User 1";
    protected static final String USER_PRIME_USERNAME = "user1";
    protected static final String USER_PRIME_EMAIL_ADDRESS = "test-user-1@gmail.com";
    protected static final String USER_PRIME_PASSWORD = "user1";
    protected static final String USER_PRIME_PHONE_NUMBER = "111_111_111";

    protected RegisterRequestDto initRegisterRequestDto() {
        return RegisterRequestDto.builder()
                .name(USER_PRIME_NAME)
                .emailAddress(USER_PRIME_EMAIL_ADDRESS)
                .username(USER_PRIME_USERNAME)
                .password(USER_PRIME_PASSWORD)
                .phoneNumber(USER_PRIME_PHONE_NUMBER)
                .build();
    }

    protected UserEntity initUserEntity() {
        return UserEntity.builder()
                .id(1L)
                .name(USER_PRIME_NAME)
                .emailAddress(USER_PRIME_EMAIL_ADDRESS)
                .username(USER_PRIME_USERNAME)
                .password(passwordEncoder.encode(USER_PRIME_PASSWORD))
                .phoneNumber(USER_PRIME_PHONE_NUMBER)
                .enabled(false)
                .userRole(UserRole.ROLE_USER)
                .followerNo(0L)
                .followingNo(0L)
                .tweetNo(0L)
                .profilePicture(fileService.convertImagePathToByteArray(DEFAULT_PROFILE_PICTURE_PATH))
                .backgroundPicture(fileService.convertImagePathToByteArray(DEFAULT_BACKGROUND_PICTURE_PATH))
                .description(null)
                .build();
    }


}
