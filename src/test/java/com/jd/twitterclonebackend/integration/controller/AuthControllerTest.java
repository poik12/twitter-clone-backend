package com.jd.twitterclonebackend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.controller.AuthController;
import com.jd.twitterclonebackend.controller.handler.AuthControllerExceptionHandler;
import com.jd.twitterclonebackend.dto.RegisterRequestDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import com.jd.twitterclonebackend.service.AuthService;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends IntegrationTestInitData {

    public MockMvc mockMvc;

    @Autowired
    public AuthController authController;

    @MockBean
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
        RegisterRequestDto registerRequestDto = initRegisterRequestDtoForPrimeUser();
        UserEntity userEntity = initPrimeUser();

        // when then
        when(authService.createUserAccount(any())).thenReturn(userEntity);

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(registerRequestDto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value(userEntity.getUsername()))
                .andExpect(jsonPath("$.name").value(userEntity.getName()))
                .andExpect(jsonPath("$.emailAddress").value(userEntity.getEmailAddress()))
                .andExpect(jsonPath("$.phoneNumber").value(userEntity.getPhoneNumber()));

        verify(authService).createUserAccount(registerRequestDto);
    }

    @Test
    void should_throwExceptionHandler_byRegisterRequestDtoWithExistingUsername() throws Exception {
        // given
        RegisterRequestDto registerRequestDto = initRegisterRequestDtoForPrimeUser();

        given(authService.createUserAccount(registerRequestDto))
                .willThrow(new UserException(
                        InvalidUserEnum.USER_ALREADY_EXISTS_WITH_EMAIL.getMessage() + registerRequestDto.getEmailAddress(),
                        HttpStatus.BAD_REQUEST
                ));

        // when then
        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(registerRequestDto))
                )
                .andDo(print())
                .andExpect(jsonPath("$.status")
                        .value(HttpStatus.BAD_REQUEST.toString().substring(4)))
                .andExpect(jsonPath("$.message")
                        .value(InvalidUserEnum.USER_ALREADY_EXISTS_WITH_EMAIL.getMessage()
                                + registerRequestDto.getEmailAddress()));
    }

    @Test
    void verifyAccount() {
    }

    @Test
    void login() {
    }

    @Test
    void refreshAccessToken() {
    }

    @Test
    void deleteAccount() {
    }
}