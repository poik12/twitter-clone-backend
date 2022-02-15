package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.domain.UserRole;
import com.jd.twitterclonebackend.dto.AuthResponse;
import com.jd.twitterclonebackend.dto.RefreshTokenRequest;
import com.jd.twitterclonebackend.dto.RegisterRequest;

public interface AuthService {
    // Create account
    UserEntity createUserAccount(RegisterRequest registerRequest);
    // Confirm account
    String confirmUserAccount(String token);
    // Delete account
    void deleteUserAccount();
    // Refresh access token
    AuthResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest);

    // TODO: not ready yet
    void changeUserRole(String username, UserRole userRole);


}
