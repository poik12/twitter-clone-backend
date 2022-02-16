package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.domain.enums.UserRole;
import com.jd.twitterclonebackend.dto.AuthResponseDto;
import com.jd.twitterclonebackend.dto.RefreshTokenRequestDto;
import com.jd.twitterclonebackend.dto.RegisterRequestDto;

public interface AuthService {
    // Create account
    UserEntity createUserAccount(RegisterRequestDto registerRequestDto);
    // Confirm account
    String confirmUserAccount(String token);
    // Delete account
    void deleteUserAccount();
    // Refresh access token
    AuthResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto);

    // TODO: not ready yet
    void changeUserRole(String username, UserRole userRole);


}
