package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.domain.UserRole;
import com.jd.twitterclonebackend.dto.RegisterRequest;
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

    public UserEntity mapFromDtoToEntity(RegisterRequest registerRequest) {

        if (Objects.isNull(registerRequest)) {
            return null;
        }

        return UserEntity.builder()
                .name(registerRequest.getName())
                .emailAddress(registerRequest.getEmailAddress())
                .username(registerRequest.getUsername())
                .password(passwordEncoder().encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .createdAt(Instant.now())
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
