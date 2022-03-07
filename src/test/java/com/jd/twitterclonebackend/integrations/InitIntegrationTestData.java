package com.jd.twitterclonebackend.integrations;

import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.PostRequestDto;
import com.jd.twitterclonebackend.entity.enums.UserRole;
import com.jd.twitterclonebackend.mapper.PostMapper;
import com.jd.twitterclonebackend.repository.PostRepository;
import com.jd.twitterclonebackend.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;

@SpringBootTest
@ContextConfiguration(locations = "/test-context.xml")
@Transactional // cleans db after each test
@RequiredArgsConstructor
public abstract class InitIntegrationTestData {

    @Autowired
    protected UserDetailsServiceImpl userDetailsService;
    @Autowired
    protected PostMapper postMapper;
    @Autowired
    protected PostRepository postRepository;

    public static final String USER_PRIME_NAME = "Test User 1";
    public static final String USER_PRIME_USERNAME = "user1";
    public static final String USER_PRIME_EMAIL_ADDRESS = "test-user-1@gmail.com";
    public static final String USER_PRIME_PASSWORD = "user1";
    public static final String USER_PRIME_PHONE_NUMBER = "111_111_111";
    public static final String USER_PRIME_TEST_DESCRIPTION = "Test Description 1";

    public static final String USER_SECOND_NAME = "Test User 2";
    public static final String USER_SECOND_USERNAME = "user2";
    public static final String USER_SECOND_EMAIL_ADDRESS = "test-user-2@gmail.com";
    public static final String USER_SECOND_PASSWORD = "user2";
    public static final String USER_SECOND_PHONE_NUMBER = "222_222_222";
    public static final String USER_SECOND_TEST_DESCRIPTION = "Test Description 2";

    public static final String POST_DESCRIPTION = "Post Description";

    protected UserEntity initDatabaseByPrimeUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(USER_PRIME_NAME);
        userEntity.setUsername(USER_PRIME_USERNAME);
        userEntity.setEmailAddress(USER_PRIME_EMAIL_ADDRESS);
        userEntity.setPassword(USER_PRIME_PASSWORD);
        userEntity.setPhoneNumber(USER_PRIME_PHONE_NUMBER);
        userEntity.setEnabled(true);
        userEntity.setUserRole(UserRole.ROLE_USER);
        userEntity.setProfilePicture(null);
        userEntity.setBackgroundPicture(null);
        userEntity.setTweetNo(0L);
        userEntity.setFollowerNo(0L);
        userEntity.setFollowingNo(0L);
        userEntity.setDescription(USER_PRIME_TEST_DESCRIPTION);
        return userEntity;
    }

    protected UserEntity initDatabaseBySecondUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(USER_SECOND_NAME);
        userEntity.setUsername(USER_SECOND_USERNAME);
        userEntity.setEmailAddress(USER_SECOND_EMAIL_ADDRESS);
        userEntity.setPassword(USER_SECOND_PASSWORD);
        userEntity.setPhoneNumber(USER_SECOND_PHONE_NUMBER);
        userEntity.setEnabled(true);
        userEntity.setUserRole(UserRole.ROLE_USER);
        userEntity.setProfilePicture(null);
        userEntity.setBackgroundPicture(null);
        userEntity.setTweetNo(0L);
        userEntity.setFollowerNo(0L);
        userEntity.setFollowingNo(0L);
        userEntity.setDescription(USER_SECOND_TEST_DESCRIPTION);
        return userEntity;
    }

    protected PostRequestDto initPostRequestDto() {
        return PostRequestDto.builder()
                .description(POST_DESCRIPTION)
                .build();
    }




}
