package com.jd.twitterclonebackend.integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jd.twitterclonebackend.dto.request.*;
import com.jd.twitterclonebackend.dto.response.*;
import com.jd.twitterclonebackend.entity.*;
import com.jd.twitterclonebackend.entity.enums.NotificationType;
import com.jd.twitterclonebackend.entity.enums.UserRole;
import com.jd.twitterclonebackend.mapper.AuthMapper;
import com.jd.twitterclonebackend.mapper.TweetMapper;
import com.jd.twitterclonebackend.repository.*;
import com.jd.twitterclonebackend.config.security.filter.jwt.RefreshTokenProvider;
import com.jd.twitterclonebackend.service.*;
import com.jd.twitterclonebackend.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

@SpringBootTest
@Transactional // cleans db after each test
@WithMockUser(
        username = "mockUsername",
        password = "mockPassword"
)
public abstract class IntegrationTestInitData {

    @Autowired
    protected UserDetailsServiceImpl userDetailsService;
    @Autowired
    protected AuthService authService;
    @Autowired
    protected FileService fileService;
    @Autowired
    protected TweetService tweetService;

    @Autowired
    protected VerificationTokenService verificationTokenService;
    @Autowired
    protected MailService mailService;
    @Autowired
    protected RefreshTokenProvider refreshTokenProvider;

    @Autowired
    protected TweetMapper tweetMapper;
    @Autowired
    protected AuthMapper authMapper;

    @Autowired
    protected TweetRepository tweetRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ImageFileRepository fileRepository;
    @Autowired
    protected VerificationTokenRepository verificationTokenRepository;
    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    protected static final String DEFAULT_PROFILE_PICTURE_PATH = "src/main/resources/images/default_profile_picture_twitter.png";
    protected static final String DEFAULT_BACKGROUND_PICTURE_PATH = "src/main/resources/images/default_background_picture_twitter.png";

    protected static final String FAKE_USERNAME = "Fake username";
    protected static final String FAKE_PASSWORD = "Fake password";

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
    protected static final String USER_UPDATE_DESCRIPTION = "Test Description 1 UPDATE";

    protected static final String VERIFICATION_TOKEN = UUID.randomUUID().toString();
    protected static final String FAKE_VERIFICATION_TOKEN = UUID.randomUUID().toString();

    protected static final String FAKE_REFRESH_TOKEN = UUID.randomUUID().toString();

    protected static final String TWEET_DESCRIPTION = "Post Description";

    protected static final String COMMENT_TEXT = "Comment text";

    private static final String MESSAGE_CONTENT = "test message content";


    protected UserEntity initDatabaseByPrimeUserDisabled() {
        UserEntity userEntity = UserEntity.builder()
                .name(USER_PRIME_NAME)
                .username(USER_PRIME_USERNAME)
                .emailAddress(USER_PRIME_EMAIL_ADDRESS)
                .password(USER_PRIME_PASSWORD)
                .phoneNumber(USER_PRIME_PHONE_NUMBER)
                .enabled(false)
                .userRole(UserRole.ROLE_USER)
                .profilePicture(fileService.convertFilePathToByteArray(DEFAULT_PROFILE_PICTURE_PATH))
                .backgroundPicture(fileService.convertFilePathToByteArray(DEFAULT_BACKGROUND_PICTURE_PATH))
                .tweetNo(0L)
                .followerNo(0L)
                .followingNo(0L)
                .description(USER_PRIME_TEST_DESCRIPTION)
                .build();
        return userRepository.save(userEntity);
    }

    protected UserEntity initDatabaseByPrimeUserEnabled() {
        UserEntity userEntity = UserEntity.builder()
                .name(USER_PRIME_NAME)
                .username(USER_PRIME_USERNAME)
                .emailAddress(USER_PRIME_EMAIL_ADDRESS)
                .password(USER_PRIME_PASSWORD)
                .phoneNumber(USER_PRIME_PHONE_NUMBER)
                .enabled(true)
                .userRole(UserRole.ROLE_USER)
                .profilePicture(null)
                .backgroundPicture(null)
                .tweetNo(0L)
                .followerNo(0L)
                .followingNo(0L)
                .followers(Collections.emptyList())
                .following(Collections.emptyList())
                .description(USER_PRIME_TEST_DESCRIPTION)
                .build();
        return userRepository.save(userEntity);
    }

    protected UserEntity initDatabaseBySecondUserEnabled() {
        UserEntity userEntity = UserEntity.builder()
                .name(USER_SECOND_NAME)
                .username(USER_SECOND_USERNAME)
                .emailAddress(USER_SECOND_EMAIL_ADDRESS)
                .password(USER_SECOND_PASSWORD)
                .phoneNumber(USER_SECOND_PHONE_NUMBER)
                .enabled(true)
                .userRole(UserRole.ROLE_USER)
                .profilePicture(fileService.convertFilePathToByteArray(DEFAULT_PROFILE_PICTURE_PATH))
                .backgroundPicture(fileService.convertFilePathToByteArray(DEFAULT_BACKGROUND_PICTURE_PATH))
                .tweetNo(0L)
                .followerNo(0L)
                .followingNo(0L)
                .followers(Collections.emptyList())
                .following(Collections.emptyList())
                .description(USER_SECOND_TEST_DESCRIPTION)
                .build();
        return userRepository.save(userEntity);
    }

    protected RegisterRequestDto initRegisterRequestDtoForPrimeUser() {
        return RegisterRequestDto.builder()
                .name(USER_PRIME_NAME)
                .emailAddress(USER_PRIME_EMAIL_ADDRESS)
                .username(USER_PRIME_USERNAME)
                .password(USER_PRIME_PASSWORD)
                .phoneNumber(USER_PRIME_PHONE_NUMBER)
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

    protected VerificationTokenEntity initVerificationTokenInDatabase(UserEntity userEntity) {
        VerificationTokenEntity verificationTokenEntity = VerificationTokenEntity.builder()
                .token(VERIFICATION_TOKEN)
                .confirmedAt(null)
                .expiresAt(Instant.now().plusSeconds(1000))
                .user(userEntity)
                .build();
        return verificationTokenRepository.save(verificationTokenEntity);
    }

    protected RefreshTokenEntity initRefreshTokenInDatabase(UserEntity userEntity) {
        // Create JWT refresh token
        String refreshToken = JWT.create()
                .withSubject(userEntity.getUsername())
                .withIssuer("Refresh Token")
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(Instant.now().plusSeconds(1000)))
                .sign(Algorithm.HMAC256("secret".getBytes()));

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(refreshToken)
                .expiresAt(Instant.now().plusSeconds(1000))
                .user(userEntity)
                .build();
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    protected UserEntity initCurrentLoggedUser() {
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userEntity.getUsername(),
                userEntity.getPassword()
        );
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
        return userEntity;
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

    protected MockMultipartFile[] initMultiPartFiles() {
        MockMultipartFile mockMultipartFileFirst = new MockMultipartFile(
                "file1.txt",
                "file1.txt",
                "text/plain",
                fileService.convertFilePathToByteArray(DEFAULT_BACKGROUND_PICTURE_PATH)
        );
        MockMultipartFile mockMultipartFileSecond = new MockMultipartFile(
                "file2.txt",
                "file2.txt",
                "text/plain",
                fileService.convertFilePathToByteArray(DEFAULT_PROFILE_PICTURE_PATH)
        );
        return new MockMultipartFile[]{
                mockMultipartFileFirst,
                mockMultipartFileSecond
        };
    }

    protected TweetRequestDto initTweetRequestDto() {
        return TweetRequestDto.builder()
                .description(TWEET_DESCRIPTION)
                .build();
    }

    protected TweetResponseDto initTweetResponseDto() {
        return TweetResponseDto.builder()
                .id(1L)
                .name(USER_PRIME_NAME)
                .username(USER_PRIME_USERNAME)
                .description(USER_PRIME_TEST_DESCRIPTION)
                .commentNo(0L)
                .createdAt(Date.from(Instant.now()))
                .tweetTimeDuration(null)
                .fileContent(null)
                .userProfilePicture(null)
                .build();
    }

    protected List<TweetEntity> initTweetListInDatabase() {
        UserEntity userEntity = initCurrentLoggedUser();
        TweetEntity tweetEntity = initTweetEntity(userEntity);
        tweetRepository.save(tweetEntity);
        return tweetRepository.findAll();
    }

    protected TweetEntity initTweetInDatabase() {
        UserEntity userEntity = initCurrentLoggedUser();
        TweetEntity tweetEntity = initTweetEntity(userEntity);
        return tweetRepository.save(tweetEntity);
    }

    protected TweetEntity initTweetEntity(UserEntity userEntity) {

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

    protected CommentRequestDto initCommentRequestDto() {
        return CommentRequestDto.builder()
                .username(USER_PRIME_USERNAME)
                .tweetId(1L)
                .text(COMMENT_TEXT)
                .build();
    }

    protected CommentResponseDto initCommentResponseDto() {
        return CommentResponseDto.builder()
                .username(USER_PRIME_USERNAME)
                .name(USER_PRIME_NAME)
                .profileImage(null)
                .tweetId(1L)
                .timeOfCreation(null)
                .text(COMMENT_TEXT)
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

    protected UserDetailsRequestDto initUserRequestDto() {
        return UserDetailsRequestDto.builder()
                .name(USER_UPDATE_NAME)
                .username(USER_UPDATE_USERNAME)
                .emailAddress(USER_UPDATE_EMAIL_ADDRESS)
                .password(USER_UPDATE_PASSWORD)
                .phoneNumber(USER_UPDATE_PHONE_NUMBER)
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

    protected MessageRequestDto initMessageRequestDto() {
        return MessageRequestDto.builder()
                .conversationId(1L)
                .content(MESSAGE_CONTENT)
                .build();
    }

    protected MessageResponseDto initMessageResponseDto() {
        return MessageResponseDto.builder()
                .content(MESSAGE_CONTENT)
                .senderUsername(USER_PRIME_USERNAME)
                .recipientUsername(USER_SECOND_USERNAME)
                .createdAt(String.valueOf(Instant.now()))
                .build();
    }

    protected NotificationResponseDto initNotificationResponseDto() {
        return NotificationResponseDto.builder()
                .id(1L)
                .name(USER_PRIME_NAME)
                .username(USER_PRIME_USERNAME)
                .userProfilePicture(null)
                .timeDuration(String.valueOf(Instant.now()))
                .type(NotificationType.FOLLOWER.getType())
                .materialId(2L)
                .build();
    }

}
