package com.jd.twitterclonebackend.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jd.twitterclonebackend.dto.request.*;
import com.jd.twitterclonebackend.dto.response.ConversationResponseDto;
import com.jd.twitterclonebackend.dto.response.EmailNotificationDto;
import com.jd.twitterclonebackend.dto.response.UserResponseDto;
import com.jd.twitterclonebackend.entity.*;
import com.jd.twitterclonebackend.entity.enums.NotificationType;
import com.jd.twitterclonebackend.entity.enums.UserRole;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ExtendWith(value = MockitoExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class UnitTestInitData {

    protected static final String USER_PRIME_NAME = "Test User 1";
    protected static final String USER_PRIME_USERNAME = "user1";
    protected static final String USER_PRIME_EMAIL_ADDRESS = "test-user-1@gmail.com";
    protected static final String USER_PRIME_PASSWORD = "user1";
    protected static final String USER_PRIME_PHONE_NUMBER = "111_111_111";
    protected static final String USER_PRIME_TEST_DESCRIPTION = "Test Description 1";

    protected static final String USER_SECOND_NAME = "Test User 2";
    protected static final String USER_SECOND_USERNAME = "user2";
    protected static final String USER_SECOND_EMAIL_ADDRESS = "test-user-2@gmail.com";
    protected static final String USER_SECOND_PASSWORD = "user2";
    protected static final String USER_SECOND_PHONE_NUMBER = "222_222_222";
    protected static final String USER_SECOND_TEST_DESCRIPTION = "Test Description 2";

    protected static final String USER_UPDATE_NAME = "Test User 1 UPDATE";
    protected static final String USER_UPDATE_USERNAME = "user1 UPDATE";
    protected static final String USER_UPDATE_EMAIL_ADDRESS = "test-user-1 UPDATE@gmail.com";
    protected static final String USER_UPDATE_PASSWORD = "user1 UPDATE";
    protected static final String USER_UPDATE_PHONE_NUMBER = "111_111_111 UPDATE";
    protected static final String USER_UPDATE_TEST_DESCRIPTION = "Test Description 1 UPDATE";

    protected static final String TWEET_DESCRIPTION = "Test Description 1";

    protected static final String COMMENT_TEXT = "Test Description 1";
    protected static final String MESSAGE_CONTENT = "Test Message 1";

    protected static final Date CREATED_AT = Date.from(Instant.now());
    protected static final Date UPDATED_AT = Date.from(Instant.now());

    protected static final String TOKEN = UUID.randomUUID().toString();
    protected static final Instant TOKEN_CONFIRMATION_TIME = Instant.now();

    protected static final String FAKE_USERNAME = "Fake username";
    protected static final String FAKE_EMAIL_ADDRESS = "Fake email address";
    protected static final String FAKE_HASHTAG_VALUE = "Fake hashtag value";
    protected static final String FAKE_TOKEN = UUID.randomUUID() + "Fake";

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
                .tweets(Collections.emptyList())
                .followerNo(0L)
                .followingNo(0L)
                .publishedNotifications(Collections.emptyList())
                .subscribedNotifications(Collections.emptyList())
                .followers(Collections.emptyList())
                .following(Collections.emptyList())
                .description(USER_PRIME_TEST_DESCRIPTION)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .build();
    }

    protected TweetEntity initTweetEntityWithoutFilesAndHashtags(UserEntity userEntity) {
        return TweetEntity.builder()
                .id(1L)
                .description(TWEET_DESCRIPTION)
                .user(userEntity)
                .commentNo(0L)
                .comments(Collections.emptyList())
                .hashtags(Collections.emptyList())
                .userLikes(Collections.emptySet())
                .images(Collections.emptyList())
                .createdAt(Date.from(Instant.now()))
                .build();
    }

    protected TweetEntity initTweetEntityWithFiles(UserEntity userEntity) {
        TweetEntity tweetEntity = initTweetEntityWithoutFilesAndHashtags(userEntity);

        return TweetEntity.builder()
                .id(1L)
                .description(TWEET_DESCRIPTION)
                .user(userEntity)
                .commentNo(0L)
                .comments(Collections.emptyList())
                .hashtags(Collections.emptyList())
                .userLikes(Collections.emptySet())
                .images(List.of(initImageFileEntity(tweetEntity)))
                .createdAt(Date.from(Instant.now()))
                .build();
    }

    private ImageFileEntity initImageFileEntity(TweetEntity tweetEntity) {

        return ImageFileEntity.builder()
                .id(1L)
                .tweet(tweetEntity)
                .content(new byte[10])
                .name("Image File Name")
                .size(10L)
                .uploadTime(Date.from(Instant.now()))
                .build();
    }

    protected HashtagEntity initHashtagEntity() {
        return HashtagEntity.builder()
                .id(1L)
                .value("#RandomHashtag")
                .tweets(Collections.emptyList())
                .createdAt(Date.from(Instant.now()))
                .build();
    }

    protected CommentRequestDto initCommentRequestDto() {
        return CommentRequestDto.builder()
                .username(USER_PRIME_USERNAME)
                .tweetId(1L)
                .text(COMMENT_TEXT)
                .build();
    }

    protected CommentEntity initCommentEntity(UserEntity userEntity, TweetEntity tweetEntity) {
        return CommentEntity.builder()
                .id(1L)
                .tweet(tweetEntity)
                .user(userEntity)
                .text(COMMENT_TEXT)
                .createdAt(Instant.now())
                .build();
    }

    protected TweetRequestDto initPostRequestDto() {
        return TweetRequestDto.builder()
                .description(TWEET_DESCRIPTION)
                .build();
    }

    protected UserDetailsRequestDto initUserRequestDto() {
        return UserDetailsRequestDto.builder()
                .name(USER_UPDATE_NAME)
                .username(USER_UPDATE_USERNAME)
                .description(USER_UPDATE_TEST_DESCRIPTION)
                .emailAddress(USER_UPDATE_EMAIL_ADDRESS)
                .password(USER_UPDATE_PASSWORD)
                .phoneNumber(USER_UPDATE_PHONE_NUMBER)
                .build();
    }

    protected UserEntity initPrimeUser() {
        return UserEntity.builder()
                .id(1L)
                .name(USER_PRIME_NAME)
                .username(USER_PRIME_USERNAME)
                .emailAddress(USER_PRIME_EMAIL_ADDRESS)
                .password(USER_PRIME_PASSWORD)
                .phoneNumber(USER_PRIME_PHONE_NUMBER)
                .enabled(false)
                .userRole(UserRole.ROLE_USER)
                .profilePicture(null)
                .backgroundPicture(null)
                .tweetNo(0L)
                .followerNo(0L)
                .followingNo(0L)
                .description(USER_PRIME_TEST_DESCRIPTION)
                .build();
    }

    protected UserEntity initSecondUser() {
        return UserEntity.builder()
                .id(2L)
                .name(USER_SECOND_NAME)
                .username(USER_SECOND_USERNAME)
                .emailAddress(USER_SECOND_EMAIL_ADDRESS)
                .password(USER_SECOND_PASSWORD)
                .phoneNumber(USER_SECOND_PHONE_NUMBER)
                .enabled(false)
                .userRole(UserRole.ROLE_USER)
                .profilePicture(null)
                .backgroundPicture(null)
                .tweetNo(0L)
                .followerNo(0L)
                .followingNo(0L)
                .description(USER_SECOND_TEST_DESCRIPTION)
                .build();
    }

    protected FollowerEntity initFollowerEntity() {
        return FollowerEntity.builder()
                .id(1L)
                .from(initPrimeUser())
                .to(initSecondUser())
                .build();
    }

    protected NotificationEntity initNotificationEntity() {
        return NotificationEntity.builder()
                .id(1L)
                .publisher(initPrimeUser())
                .subscriber(initSecondUser())
                .notificationType(NotificationType.TWEET)
                .materialId(1L)
                .createdAt(Date.from(Instant.now()))
                .build();
    }

    protected ConversationEntity initConversationEntity() {
        return ConversationEntity.builder()
                .id(1L)
                .creator(initPrimeUser())
                .participant(initSecondUser())
                .messages(Collections.emptyList())
                .latestMessageTime(Date.from(Instant.now()))
                .latestMessageContent(MESSAGE_CONTENT)
                .latestMessageRead(true)
                .createdAt(Date.from(Instant.now()))
                .updatedAt(Date.from(Instant.now()))
                .build();
    }

    protected MessageEntity initMessageEntity() {
        UserEntity primeUser = initPrimeUser();
        UserEntity secondUser = initSecondUser();
        ConversationEntity conversationEntity = initConversationEntity();

        return MessageEntity.builder()
                .id(1L)
                .content(MESSAGE_CONTENT)
                .conversation(conversationEntity)
                .sender(primeUser)
                .recipient(secondUser)
                .createdAt(Date.from(Instant.now()))
                .build();
    }

    protected ConversationRequestDto initConversationRequestDto() {
        return ConversationRequestDto.builder()
                .participantUsername(USER_PRIME_USERNAME)
                .build();
    }

    protected ConversationResponseDto initConversationResponseDto() {
        return ConversationResponseDto.builder()
                .id(1L)
                .participantName(USER_PRIME_NAME)
                .participantUsername(USER_PRIME_USERNAME)
                .participantProfilePicture(null)
                .creatorName(USER_SECOND_NAME)
                .creatorUsername(USER_SECOND_USERNAME)
                .creatorProfilePicture(null)
                .latestMessageTime(String.valueOf(Instant.now()))
                .latestMessageContent(MESSAGE_CONTENT)
                .latestMessageRead(true)
                .build();
    }

    protected UserResponseDto initUserResponseDto(UserEntity userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .tweetNo(userEntity.getTweetNo())
                .followingNo(userEntity.getFollowingNo())
                .followerNo(userEntity.getFollowerNo())
                .userProfilePicture(userEntity.getProfilePicture())
                .userBackgroundPicture(userEntity.getBackgroundPicture())
                .description(userEntity.getDescription())
                .build();
    }

    protected <T> String initRequestDtoAsJson(T requestDto) {
        String requestJSON = null;
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            requestJSON = objectWriter.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return requestJSON;
    }

    protected EmailNotificationDto initEmailNotificationDto() {
        return EmailNotificationDto.builder()
                .emailSubject("Email Subject")
                .emailRecipient(USER_PRIME_EMAIL_ADDRESS)
                .recipientName(USER_PRIME_NAME)
                .activationLink("LINK")
                .build();
    }

    protected RefreshTokenEntity initRefreshTokenEntity(UserEntity userEntity) {

        return RefreshTokenEntity.builder()
                .id(2L)
                .token(TOKEN)
                .user(userEntity)
                .createdAt(CREATED_AT)
                .expiresAt(CREATED_AT.toInstant().plusSeconds(1000))
                .build();
    }

    protected VerificationTokenEntity initVerificationTokenEntity(UserEntity userEntity) {
        return VerificationTokenEntity.builder()
                .id(2L)
                .token(TOKEN)
                .user(userEntity)
                .createdAt(CREATED_AT)
                .expiresAt(CREATED_AT.toInstant().plusSeconds(1000))
                .confirmedAt(null)
                .build();
    }

}
