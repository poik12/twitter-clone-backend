package com.jd.twitterclonebackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.domain.RefreshTokenEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.AuthResponse;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SecurityResponse {



    public void successfulAuthenticationResponse(HttpServletResponse response,
                                                 String accessToken,
                                                 Instant expirationTime,
                                                 String refreshToken,
                                                 String username) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        AuthResponse authResponse = AuthResponse.builder()
                .username(username)
                .authenticationToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(String.valueOf(expirationTime))
                .build();

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                authResponse
        );
    }

    public void successfulRefreshTokenResponse(HttpServletResponse response,
                                                 String accessToken,
                                                 String refreshToken,
                                                 String info) throws IOException {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                tokens
        );
    }

    public void failedAuthorizationResponse(HttpServletResponse response,
                                            Exception exception,
                                            String header) throws IOException {
        response.setHeader("authorization error", exception.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());

        Map<String, String> error = new HashMap<>();
        error.put("error_message", exception.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                error
        );
    }

}
