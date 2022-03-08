package com.jd.twitterclonebackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.dto.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SecurityResponse {

    public static void successfulAuthenticationResponse(HttpServletResponse response,
                                                 String accessToken,
                                                 Instant expirationTime,
                                                 String refreshToken,
                                                 String username) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        AuthResponseDto authResponseDto = AuthResponseDto.builder()
                .username(username)
                .authenticationToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(String.valueOf(expirationTime))
                .build();

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                authResponseDto
        );
    }

    public static void successfulRefreshTokenResponse(HttpServletResponse response,
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

    public static void failedAuthorizationResponse(HttpServletResponse response,
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
