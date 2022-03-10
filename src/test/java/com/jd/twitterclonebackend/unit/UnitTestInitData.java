package com.jd.twitterclonebackend.unit;

import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.dto.request.PostRequestDto;
import com.jd.twitterclonebackend.dto.request.RegisterRequestDto;
import com.jd.twitterclonebackend.dto.request.UserDetailsRequestDto;
import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.enums.UserRole;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@ExtendWith(value = MockitoExtension.class)
public abstract class UnitTestInitData {

    protected static final String USER_PRIME_NAME = "Test User 1";
    protected static final String USER_PRIME_USERNAME = "user1";
    protected static final String USER_PRIME_EMAIL_ADDRESS = "test-user-1@gmail.com";
    protected static final String USER_PRIME_PASSWORD = "user1";
    protected static final String USER_PRIME_PHONE_NUMBER = "111_111_111";
    protected static final String USER_PRIME_TEST_DESCRIPTION = "Test Description 1";

    protected static final String USER_UPDATE_NAME = "Test User 1 UPDATE";
    protected static final String USER_UPDATE_USERNAME = "user1 UPDATE";
    protected static final String USER_UPDATE_EMAIL_ADDRESS = "test-user-1 UPDATE@gmail.com";
    protected static final String USER_UPDATE_PASSWORD = "user1 UPDATE";
    protected static final String USER_UPDATE_PHONE_NUMBER = "111_111_111 UPDATE";
    protected static final String USER_UPDATE_TEST_DESCRIPTION = "Test Description 1 UPDATE";

    protected static final String POST_DESCRIPTION = "Test Description 1";

    protected static final String COMMENT_TEXT = "Test Description 1";

    protected static final Date CREATED_AT = Date.from(Instant.now());
    protected static final Date UPDATED_AT = Date.from(Instant.now());

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
                .password(USER_PRIME_PASSWORD)
                .phoneNumber(USER_PRIME_PHONE_NUMBER)
                .enabled(true)
                .userRole(UserRole.ROLE_USER)
                .profilePicture("byte".getBytes())
                .backgroundPicture("byte".getBytes())
                .tweetNo(0L)
                .followerNo(0L)
                .followingNo(0L)
                .followers(Collections.emptyList())
                .following(Collections.emptyList())
                .description(USER_PRIME_TEST_DESCRIPTION)
                .createdAt(Date.from(Instant.now()))
                .updatedAt(Date.from(Instant.now()))
                .build();
    }

    protected PostEntity initPostEntity(UserEntity userEntity) {
        return PostEntity.builder()
                .id(1L)
                .description(POST_DESCRIPTION)
                .commentNo(0L)
                .createdAt(Date.from(Instant.now()))
                .comments(Collections.emptyList())
                .user(userEntity)
                .build();
    }

    protected CommentRequestDto initCommentRequestDto() {
        return CommentRequestDto.builder()
                .username(USER_PRIME_USERNAME)
                .postId(1L)
                .text(COMMENT_TEXT)
                .build();
    }

    protected CommentEntity initCommentEntity(UserEntity userEntity, PostEntity postEntity) {
        return CommentEntity.builder()
                .id(1L)
                .post(postEntity)
                .user(userEntity)
                .text(COMMENT_TEXT)
                .createdAt(Instant.now())
                .build();
    }

    protected PostRequestDto initPostRequestDto() {
        return PostRequestDto.builder()
                .description(POST_DESCRIPTION)
                .build();
    }

    protected UserDetailsRequestDto initUserRequestDto() {
        return UserDetailsRequestDto.builder()
                .name(USER_UPDATE_NAME)
                .username(USER_UPDATE_USERNAME)
                .emailAddress(USER_UPDATE_EMAIL_ADDRESS)
                .password(USER_UPDATE_PASSWORD)
                .phoneNumber(USER_UPDATE_PHONE_NUMBER)
                .build();
    }


}
