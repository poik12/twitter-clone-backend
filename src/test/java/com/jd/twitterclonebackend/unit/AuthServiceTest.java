package com.jd.twitterclonebackend.unit;

import com.jd.twitterclonebackend.dto.RegisterRequestDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.AuthMapper;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.security.jwt.RefreshTokenProvider;
import com.jd.twitterclonebackend.service.AuthService;
import com.jd.twitterclonebackend.service.MailService;
import com.jd.twitterclonebackend.service.VerificationTokenService;
import com.jd.twitterclonebackend.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest extends UnitTestInitData {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private MailService mailService;
    @Mock
    private VerificationTokenService verificationTokenService;
    @Mock
    private RefreshTokenProvider refreshTokenProvider;

    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthMapper authMapper;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

//    @BeforeEach
//    public void setup() {
//        authService = new AuthServiceImpl(
//                userRepository,
//                mailService,
//                userDetailsService,
//                verificationTokenService,
//                authMapper,
//                refreshTokenProvider
//        );
//    }

    @Test
    void should_createUserAccount() {
        // given
        RegisterRequestDto registerRequestDto = initRegisterRequestDto();
        UserEntity mockUserEntity = initUserEntity();

        // when
        UserEntity userEntity = authService.createUserAccount(registerRequestDto);

        // then
        assertThat(userEntity).isEqualTo(mockUserEntity);

    }


}