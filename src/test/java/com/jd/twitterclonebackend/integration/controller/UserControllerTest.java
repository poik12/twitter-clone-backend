package com.jd.twitterclonebackend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.controller.UserController;
import com.jd.twitterclonebackend.controller.handler.UserControllerExceptionHandler;
import com.jd.twitterclonebackend.dto.UserResponseDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import com.jd.twitterclonebackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends IntegrationTestInitData {

    public MockMvc mockMvc;

    @Autowired
    public UserController userController;

    @MockBean
    public UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new UserControllerExceptionHandler())
                .build();
    }

    @Test
    void should_getUserDetails_byUsername() throws Exception {
        // given
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();
        UserResponseDto userResponseDto = initUserResponseDto(userEntity);

        // when then
        when(userService.getUserByUsername(any())).thenReturn(userResponseDto);

        mockMvc.perform(
                        get("/users/{username}", userEntity.getUsername())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userEntity.getId()))
                .andExpect(jsonPath("$.name").value(userEntity.getName()))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.tweetNo").value(userEntity.getTweetNo()))
                .andExpect(jsonPath("$.followingNo").value(userEntity.getFollowingNo()))
                .andExpect(jsonPath("$.followerNo").value(userEntity.getFollowerNo()))
                .andExpect(jsonPath("$.description").value(userEntity.getDescription()));

        verify(userService).getUserByUsername(userEntity.getUsername());
    }

    @Test
    void should_throwExceptionHandler_byNotExistingUsername() throws Exception {
        // given
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();
        UserResponseDto userResponseDto = initUserResponseDto(userEntity);

        given(userService.getUserByUsername(userEntity.getUsername()))
                .willThrow(new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + userEntity.getUsername(),
                        HttpStatus.NOT_FOUND
                ));

        // when then
        mockMvc.perform(
                        get("/users/{username}", userEntity.getUsername())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(jsonPath("$.status")
                        .value(HttpStatus.NOT_FOUND.toString().substring(4)))
                .andExpect(jsonPath("$.message")
                        .value(InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage()
                                + userEntity.getUsername()));
    }


    @Test
    void getUsers() {
    }

    @Test
    void updateUserDetails() {
    }

    @Test
    void checkIfUserIfFollowed() {
    }

    @Test
    void followUser() {
    }

    @Test
    void unfollowUser() {
    }
}