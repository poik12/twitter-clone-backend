package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.enums.UserRole;
import com.jd.twitterclonebackend.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final PasswordEncoder passwordEncoder;

    public UserEntity mapFromDtoToEntity(RegisterRequestDto registerRequestDto) {

        if (Objects.isNull(registerRequestDto)) {
            return null;
        }

        return UserEntity.builder()
                .name(registerRequestDto.getName())
                .emailAddress(registerRequestDto.getEmailAddress())
                .username(registerRequestDto.getUsername())
                .password(passwordEncoder().encode(registerRequestDto.getPassword()))
                .phoneNumber(registerRequestDto.getPhoneNumber())
                .enabled(false)
                .userRole(UserRole.ROLE_USER)
                .followerNo(0L)
                .followingNo(0L)
                .tweetNo(0L)
                .userProfilePicture(null)
                .userBackgroundPicture(null)
                .description(null)
                .build();

    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
