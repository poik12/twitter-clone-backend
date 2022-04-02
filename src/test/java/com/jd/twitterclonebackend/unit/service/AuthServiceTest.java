package com.jd.twitterclonebackend.unit.service;

import com.jd.twitterclonebackend.dto.request.RegisterRequestDto;
import com.jd.twitterclonebackend.dto.response.EmailNotificationDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.AuthMapper;
import com.jd.twitterclonebackend.repository.RefreshTokenRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.config.security.filter.jwt.RefreshTokenProvider;
import com.jd.twitterclonebackend.repository.VerificationTokenRepository;
import com.jd.twitterclonebackend.service.AuthService;
import com.jd.twitterclonebackend.service.MailService;
import com.jd.twitterclonebackend.service.VerificationTokenService;
import com.jd.twitterclonebackend.service.impl.AuthServiceImpl;
import com.jd.twitterclonebackend.service.impl.UserDetailsServiceImpl;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest extends UnitTestInitData {

    private AuthService underTest;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private MailService mailService;
    @Mock
    private VerificationTokenService verificationTokenService;
    @Mock
    private RefreshTokenProvider refreshTokenProvider;
    @Mock
    private AuthMapper authMapper;

    @BeforeEach
    public void setUp() {
        underTest = new AuthServiceImpl(
                userRepository,
                refreshTokenRepository,
                verificationTokenRepository,
                mailService,
                userDetailsService,
                verificationTokenService,
                authMapper,
                refreshTokenProvider
        );
    }

    @Test
    @DisplayName(value = "Should create UserEntity from RegisterRequestDto ")
    void should_createUserAccount_byRegisterRequestDto() {
        // given
        RegisterRequestDto registerRequestDto = initRegisterRequestDto();
        UserEntity userEntity = initUserEntity();
        EmailNotificationDto emailNotificationDto = initEmailNotificationDto();

        // when
        when(authMapper.mapFromDtoToEntity(any()))
                .thenReturn(userEntity);
        when(verificationTokenService.generateVerificationToken(any()))
                .thenReturn(UUID.randomUUID().toString());
        when(mailService.createActivationEmail(any(), any()))
                .thenReturn(emailNotificationDto);
        doNothing().when(mailService).sendEmail(any());
        var result = underTest.createUserAccount(registerRequestDto);

        // then
        assertAll(
                () -> {
                    verify(authMapper).mapFromDtoToEntity(any());
                    verify(userRepository).save(any());
                    verify(verificationTokenService).generateVerificationToken(any());
                    verify(mailService).createActivationEmail(any(), any());
                    verify(mailService).sendEmail(any());
                    assertSame(userEntity, result);
                }
        );
    }



}