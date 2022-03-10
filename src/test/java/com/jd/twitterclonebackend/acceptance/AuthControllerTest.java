package com.jd.twitterclonebackend.acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.controller.AuthController;
import com.jd.twitterclonebackend.controller.handler.AuthControllerExceptionHandler;
import com.jd.twitterclonebackend.dto.request.RegisterRequestDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends AcceptanceTestInitData {

    public MockMvc mockMvc;

    @Autowired
    public AuthController authController;

    @Autowired
    public AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new AuthControllerExceptionHandler())
                .build();
    }

    @Test
    void should_createAccount_byRegisterRequestDto() throws Exception {
        // given
        RegisterRequestDto registerRequestDto = initRegisterRequestDto();
        UserEntity userEntity = initUserEntity();

        // TODO: error occurrence because of mail sending

        // when then
        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(registerRequestDto))
                )
//                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userEntity.getId()))
                .andExpect(jsonPath("$.name").value(userEntity.getName()))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
//                .andExpect(jsonPath("$.password").value(userEntity.getPassword()))
                .andExpect(jsonPath("$.emailAddress").value(userEntity.getEmailAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(userEntity.getPhoneNumber()))
//                .andExpect(jsonPath("$.profilePicture").value(userEntity.getProfilePicture()))
//                .andExpect(jsonPath("$.backgroundPicture").value(userEntity.getBackgroundPicture()))
                .andExpect(jsonPath("$.tweetNo").value(userEntity.getTweetNo()))
                .andExpect(jsonPath("$.followerNo").value(userEntity.getFollowerNo()))
                .andExpect(jsonPath("$.followingNo").value(userEntity.getFollowingNo()))
                .andExpect(jsonPath("$.description").value(userEntity.getDescription()))
                .andExpect(jsonPath("$.enabled").value(userEntity.getEnabled()))
                .andExpect(jsonPath("$.userRole").value(userEntity.getUserRole().name()));

    }


}
