package com.jd.twitterclonebackend.integrations.service;

import com.jd.twitterclonebackend.entity.enums.UserRole;
import com.jd.twitterclonebackend.integrations.InitIntegrationTestData;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UserDetailsServiceImplTest extends InitIntegrationTestData {


    @Test
    void should_loadUserByUsername_byUsername() {
        // given
        initDatabaseByPrimeUser();

        // when
        UserDetails result = userDetailsService.loadUserByUsername(USER_PRIME_USERNAME);

        // then
        assertThat(result.getUsername()).isEqualTo(USER_PRIME_USERNAME);
        assertThat(result.getPassword()).isEqualTo(USER_PRIME_PASSWORD);
        assertThat(result.getAuthorities()).isEqualTo(UserRole.ROLE_USER);
    }



}