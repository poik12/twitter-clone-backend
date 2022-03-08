package com.jd.twitterclonebackend.integration.service;

import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.integration.InitIntegrationTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UserDetailsServiceImplTest extends InitIntegrationTestData {


    @Test
    void should_loadUser_byUsername() {
        // given
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();

        // when
        UserDetails result = userDetailsService.loadUserByUsername(USER_PRIME_USERNAME);

        // then
        assertThat(result.getUsername()).isEqualTo(USER_PRIME_USERNAME);
        assertThat(result.getPassword()).isEqualTo(USER_PRIME_PASSWORD);
    }

    @Test
    void should_throwUserException_whenLoadUserByWrongUsername() {
        // given
        initDatabaseByPrimeUserEnabled();

        // when
        UserException result = assertThrows(
                UserException.class,
                () -> userDetailsService.loadUserByUsername(FAKE_USERNAME)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getMessage())
                .isEqualTo(InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + FAKE_USERNAME);
    }

    @Test
    void should_getCurrentLoggedUser_fromSecurityContext() {
        // given
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userEntity.getUsername(),
                userEntity.getPassword()
        );
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        // when
        UserEntity result = userDetailsService.currentLoggedUserEntity();

        // then
        assertThat(result).isEqualTo(userEntity);
    }

    @Test
    void should_throwUserException_whenUserNotFoundInSecurityContext() {
        // given
        initDatabaseByPrimeUserEnabled();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                FAKE_USERNAME,
                FAKE_PASSWORD
        );
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        // when
        UserException result = assertThrows(
                UserException.class,
                () -> userDetailsService.currentLoggedUserEntity()
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getMessage())
                .isEqualTo(InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + FAKE_USERNAME);
    }


}