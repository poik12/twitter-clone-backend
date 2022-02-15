package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.enums.UserRole;
import com.jd.twitterclonebackend.dto.AuthResponse;
import com.jd.twitterclonebackend.dto.RefreshTokenRequest;
import com.jd.twitterclonebackend.dto.RegisterRequestDto;

public interface AuthService {
    // Create account
    UserEntity createUserAccount(RegisterRequestDto registerRequestDto);
    // Confirm account
    String confirmUserAccount(String token);
    // Delete account
    void deleteUserAccount();
    // Refresh access token
    AuthResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest);

    // TODO: not ready yet
    void changeUserRole(String username, UserRole userRole);


}
