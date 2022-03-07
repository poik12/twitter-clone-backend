package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.AuthResponseDto;
import com.jd.twitterclonebackend.dto.EmailConfirmationDto;
import com.jd.twitterclonebackend.dto.RefreshTokenRequestDto;
import com.jd.twitterclonebackend.dto.RegisterRequestDto;

public interface AuthService {
    // Create account
    UserEntity createUserAccount(RegisterRequestDto registerRequestDto);
    // Confirm account
    EmailConfirmationDto confirmUserAccount(String token);
    // Delete account
    void deleteUserAccount();
    // Refresh access token
    AuthResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto);
}
