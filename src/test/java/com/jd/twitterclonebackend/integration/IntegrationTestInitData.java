package com.jd.twitterclonebackend.integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jd.twitterclonebackend.dto.request.RegisterRequestDto;
import com.jd.twitterclonebackend.dto.request.UserDetailsRequestDto;
import com.jd.twitterclonebackend.dto.response.UserResponseDto;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.RefreshTokenEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.request.PostRequestDto;
import com.jd.twitterclonebackend.entity.VerificationTokenEntity;
import com.jd.twitterclonebackend.entity.enums.UserRole;
import com.jd.twitterclonebackend.mapper.AuthMapper;
import com.jd.twitterclonebackend.mapper.PostMapper;
import com.jd.twitterclonebackend.repository.*;
import com.jd.twitterclonebackend.config.security.jwt.RefreshTokenProvider;
import com.jd.twitterclonebackend.service.*;
import com.jd.twitterclonebackend.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    protected PostService postService;

    @Autowired
    protected VerificationTokenService verificationTokenService;
    @Autowired
    protected MailService mailService;
    @Autowired
    protected RefreshTokenProvider refreshTokenProvider;

    @Autowired
    protected PostMapper postMapper;
    @Autowired
    protected AuthMapper authMapper;

    @Autowired
    protected PostRepository postRepository;
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
    protected static final String USER_UPDATE_TEST_DESCRIPTION = "Test Description 1 UPDATE";

    protected static final String VERIFICATION_TOKEN = UUID.randomUUID().toString();
    protected static final String FAKE_VERIFICATION_TOKEN = UUID.randomUUID().toString();

    protected static final String FAKE_REFRESH_TOKEN = UUID.randomUUID().toString();



    protected static final String POST_DESCRIPTION = "Post Description";

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
                .expiresAt( Instant.now().plusSeconds(1000))
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

    protected MultipartFile initMultiPartFile() {
        byte[] content = fileService.convertFilePathToByteArray(DEFAULT_BACKGROUND_PICTURE_PATH);
        return new MockMultipartFile(
                "file.txt",
                "file.txt",
                "text/plain",
                content
        );
    }

    protected PostRequestDto initPostRequestDto() {
            return PostRequestDto.builder()
                .description(POST_DESCRIPTION)
                .build();
    }


    protected List<PostEntity> initPostsInDatabase() {
        initCurrentLoggedUser();
        String postRequestAsJson = initRequestDtoAsJson(initPostRequestDto());
        MultipartFile file = initMultiPartFile();

        postService.addPost(file, postRequestAsJson);
        postService.addPost(file, postRequestAsJson);
        postService.addPost(null, postRequestAsJson);

        return postRepository.findAll();
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

}
