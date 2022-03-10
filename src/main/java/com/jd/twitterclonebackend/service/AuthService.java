package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.response.AuthResponseDto;
import com.jd.twitterclonebackend.dto.response.EmailConfirmationDto;
import com.jd.twitterclonebackend.dto.request.RefreshTokenRequestDto;
import com.jd.twitterclonebackend.dto.request.RegisterRequestDto;

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
