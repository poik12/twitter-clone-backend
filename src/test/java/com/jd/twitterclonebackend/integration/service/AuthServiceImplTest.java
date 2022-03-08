package com.jd.twitterclonebackend.integration.service;

import com.jd.twitterclonebackend.dto.AuthResponseDto;
import com.jd.twitterclonebackend.dto.EmailConfirmationDto;
import com.jd.twitterclonebackend.dto.RefreshTokenRequestDto;
import com.jd.twitterclonebackend.dto.RegisterRequestDto;
import com.jd.twitterclonebackend.entity.RefreshTokenEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.VerificationTokenEntity;
import com.jd.twitterclonebackend.exception.TokenException;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidTokenEnum;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.integration.InitIntegrationTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceImplTest extends InitIntegrationTestData {

    @Test
    void should_createUserAccount_byRegisterRequestDto() {
        // given
        RegisterRequestDto registerRequestDto = initRegisterRequestDtoForPrimeUser();
        UserEntity userEntity = initPrimeUser();

        // when
        UserEntity result = authService.createUserAccount(registerRequestDto);

        // then
        assertThat(result).isEqualTo(userEntity);

    }

    @Test
    void should_throwUserException_whenUsernameFromRegisterRequestDtoExists() {
        // given
        RegisterRequestDto registerRequestDto = initRegisterRequestDtoForPrimeUser();
        initPrimeUser();

        // when
        UserException result = assertThrows(
                UserException.class,
                () -> authService.createUserAccount(registerRequestDto)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getMessage())
                .isEqualTo(InvalidUserEnum.USER_ALREADY_EXISTS_WITH_USERNAME + registerRequestDto.getUsername());
    }

    @Test
    void should_throwUserException_whenEMailAddressFromRegisterRequestDtoExists() {
        // given
        RegisterRequestDto registerRequestDto = initRegisterRequestDtoForPrimeUser();
        UserEntity userEntity = initPrimeUser();

        // when
        UserException result = assertThrows(
                UserException.class,
                () -> authService.createUserAccount(registerRequestDto)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getMessage())
                .isEqualTo(InvalidUserEnum.USER_ALREADY_EXISTS_WITH_EMAIL + registerRequestDto.getEmailAddress());
    }

    @Test
    void should_confirmUserAccount_byVerificationToken() {
        // given
        VerificationTokenEntity verificationTokenEntity =
                initVerificationTokenInDatabase(initDatabaseByPrimeUserDisabled());

        // when
        EmailConfirmationDto result = authService.confirmUserAccount(VERIFICATION_TOKEN);

        // then
        assertThat(result.getMessage()).isEqualTo("Email has been confirmed");
        assertThat(verificationTokenEntity.getUser().getEnabled()).isEqualTo(true);
        // TODO: verification token not confirmed something wrong
        // TODO: probably something wrong with sql - hibernate has different dialect than MySql
    }

    @Test
    void should_throwUserException_whenTokenFotFound() {
        // given
        initVerificationTokenInDatabase(initDatabaseByPrimeUserDisabled());

        // when
        TokenException result = assertThrows(TokenException.class,
                () -> authService.confirmUserAccount(FAKE_VERIFICATION_TOKEN)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getMessage()).isEqualTo(InvalidTokenEnum.INVALID_VERIFICATION_TOKEN.getMessage());
    }

    @Test
    void should_throwTokenException_whenEmailAlreadyConfirmed() {
        // given
        VerificationTokenEntity verificationTokenEntity = initVerificationTokenInDatabase(initDatabaseByPrimeUserDisabled());
        verificationTokenEntity.setConfirmedAt(Instant.now());

        // when
        TokenException result = assertThrows(TokenException.class,
                () -> authService.confirmUserAccount(VERIFICATION_TOKEN)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getMessage()).isEqualTo(InvalidTokenEnum.EMAIL_ALREADY_CONFIRMED.getMessage());
    }

    @Test
    void should_throwUserException_whenUsernameNotFound() {
        // given
        VerificationTokenEntity verificationTokenEntity = initVerificationTokenInDatabase(initDatabaseByPrimeUserDisabled());
        UserEntity userEntity = initSecondUser();
        verificationTokenEntity.setUser(userEntity);

        // when
        UserException result = assertThrows(UserException.class,
                () -> authService.confirmUserAccount(VERIFICATION_TOKEN)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getMessage())
                .isEqualTo(InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + userEntity.getUsername());
    }

    @Test
    void should_throwUserException_whenUserEnabled() {
        // given
        VerificationTokenEntity verificationTokenEntity = initVerificationTokenInDatabase(initDatabaseByPrimeUserDisabled());
        verificationTokenEntity.getUser().setEnabled(true);

        // when
        TokenException result = assertThrows(TokenException.class,
                () -> authService.confirmUserAccount(VERIFICATION_TOKEN)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getMessage())
                .isEqualTo(InvalidTokenEnum.USER_ALREADY_CONFIRMED.getMessage());
    }

    @Test
    void should_deleteAccountLoggedUser_withVerificationToken() {
        // given
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();
        VerificationTokenEntity verificationTokenEntity = initVerificationTokenInDatabase(userEntity);
        RefreshTokenEntity refreshTokenEntity = initRefreshTokenInDatabase(userEntity);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userEntity.getUsername(),
                userEntity.getPassword()
        );
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        assertThat(userRepository.findAll()).hasSize(1);
        assertThat(verificationTokenRepository.findAll()).hasSize(1);
        assertThat(refreshTokenRepository.findAll()).hasSize(1);
        assertThat(verificationTokenEntity.getUser().getId()).isEqualTo(1);
        assertThat(refreshTokenEntity.getUser().getId()).isEqualTo(1);

        // when
        authService.deleteUserAccount();

        // then
        assertThat(userRepository.findAll()).hasSize(0);
        assertThat(verificationTokenRepository.findAll()).hasSize(0);
        assertThat(refreshTokenRepository.findAll()).hasSize(0);
        assertThat(userRepository.findByUsername(userEntity.getUsername()))
                .isEmpty();
    }

//    @Test
//    void should_createRefreshToken_byUser() {
//        // given
//        UserEntity userEntity = initDatabaseByPrimeUserEnabled();
//
//        // when
//
//        // then
//
//    }

    @Test
    void should_refreshAccessToken_byAccessTokenRequestDto() {
        // given
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();
        RefreshTokenEntity refreshTokenEntity = initRefreshTokenInDatabase(userEntity);
        RefreshTokenRequestDto refreshTokenRequestDto = RefreshTokenRequestDto.builder()
                .refreshToken(refreshTokenEntity.getToken())
                .username(userEntity.getUsername())
                .build();

        // when
        AuthResponseDto result = authService.refreshAccessToken(refreshTokenRequestDto);

        // then
        assertThat(result.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(result.getAuthenticationToken()).isNotNull();
        assertThat(result.getRefreshToken()).isEqualTo(refreshTokenEntity.getToken());
    }

    @Test
    void should_throwTokenException_whenInvalidRefreshToken() {
        // given
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();
        RefreshTokenEntity refreshTokenEntity = initRefreshTokenInDatabase(userEntity);
        RefreshTokenRequestDto refreshTokenRequestDto = RefreshTokenRequestDto.builder()
                .refreshToken(FAKE_REFRESH_TOKEN)
                .username(userEntity.getUsername())
                .build();

        // when
        TokenException result = assertThrows(
                TokenException.class,
                () -> authService.refreshAccessToken(refreshTokenRequestDto)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getMessage()).isEqualTo(InvalidTokenEnum.INVALID_REFRESH_TOKEN.getMessage());
    }

    @Test
    void should_throwTokenException_whenRefreshTokenIsExpired() {
        // given
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();
        RefreshTokenEntity refreshTokenEntity = initRefreshTokenInDatabase(userEntity);
        RefreshTokenRequestDto refreshTokenRequestDto = RefreshTokenRequestDto.builder()
                .refreshToken(refreshTokenEntity.getToken())
                .username(userEntity.getUsername())
                .build();
        refreshTokenEntity.setExpiresAt(Instant.now());
        refreshTokenRepository.save(refreshTokenEntity);

        // when
        TokenException result = assertThrows(
                TokenException.class,
                () -> authService.refreshAccessToken(refreshTokenRequestDto)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getMessage()).isEqualTo(InvalidTokenEnum.REFRESH_TOKEN_EXPIRED.getMessage());
    }

    @Test
    void should_throwUserException_whenUsernameFromTokenWasNotFound() {
        // given
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();
        RefreshTokenEntity refreshTokenEntity = initRefreshTokenInDatabase(userEntity);

        RefreshTokenRequestDto refreshTokenRequestDto = RefreshTokenRequestDto.builder()
                .refreshToken(refreshTokenEntity.getToken())
                .username(userEntity.getUsername())
                .build();

        userEntity.setUsername(FAKE_USERNAME);
        assertThat(userRepository.findByUsername(FAKE_USERNAME)).isNotEmpty();

        // when
        UserException result = assertThrows(
                UserException.class,
                () -> authService.refreshAccessToken(refreshTokenRequestDto)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getMessage()).isEqualTo(InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + USER_PRIME_USERNAME);

    }



}