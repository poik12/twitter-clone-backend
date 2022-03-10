package com.jd.twitterclonebackend.acceptance;

import com.jd.twitterclonebackend.dto.request.RegisterRequestDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.enums.UserRole;
import com.jd.twitterclonebackend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AcceptanceTestInitData {

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
    protected static final String USER_PRIME_TEST_DESCRIPTION = "Test Description 1";

    protected static final String POST_DESCRIPTION = "Test Description 1";

    protected static final String COMMENT_TEXT = "Test Description 1";

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
                .username(USER_PRIME_USERNAME)
                .emailAddress(USER_PRIME_EMAIL_ADDRESS)
                .password(passwordEncoder.encode(USER_PRIME_PASSWORD))
                .phoneNumber(USER_PRIME_PHONE_NUMBER)
                .enabled(false)
                .userRole(UserRole.ROLE_USER)
                .profilePicture(fileService.convertImagePathToByteArray(DEFAULT_PROFILE_PICTURE_PATH))
                .backgroundPicture(fileService.convertImagePathToByteArray(DEFAULT_BACKGROUND_PICTURE_PATH))
                .tweetNo(0L)
                .followerNo(0L)
                .followingNo(0L)
                .description(null)
                .createdAt(Date.from(Instant.now()))
                .updatedAt(Date.from(Instant.now()))
                .build();
    }

}
