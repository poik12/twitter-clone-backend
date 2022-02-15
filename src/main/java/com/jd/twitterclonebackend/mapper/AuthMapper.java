package com.jd.twitterclonebackend.mapper;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.RegisterRequestDto;
import com.jd.twitterclonebackend.enums.UserRole;
import com.jd.twitterclonebackend.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    private static final String DEFAULT_PROFILE_PICTURE_PATH = "src/main/resources/images/default_profile_picture_twitter.png";
    private static final String DEFAULT_BACKGROUND_PICTURE_PATH = "src/main/resources/images/default_background_picture_twitter.png";

    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    public UserEntity mapFromDtoToEntity(RegisterRequestDto registerRequestDto) {

        if (Objects.isNull(registerRequestDto)) {
            return null;
        }

        return UserEntity.builder()
                .name(registerRequestDto.getName())
                .emailAddress(registerRequestDto.getEmailAddress())
                .username(registerRequestDto.getUsername())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .phoneNumber(registerRequestDto.getPhoneNumber())
                .enabled(false)
                .userRole(UserRole.ROLE_USER)
                .followerNo(0L)
                .followingNo(0L)
                .tweetNo(0L)
                .userProfilePicture(fileService.convertImagePathToByteArray(DEFAULT_PROFILE_PICTURE_PATH))
                .userBackgroundPicture(fileService.convertImagePathToByteArray(DEFAULT_BACKGROUND_PICTURE_PATH))
                .description(null)
                .build();

    }

//    private PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}
