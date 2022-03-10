package com.jd.twitterclonebackend.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.dto.response.AuthResponseDto;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
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

    public static void failedAuthenticationResponse(HttpServletResponse response,
                                                    Exception exception,
                                                    String header) throws IOException {
        response.setHeader("AUTHORIZATION ERROR", exception.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> failedAuthResponse = new HashMap<>();
        failedAuthResponse.put("header", header);
        failedAuthResponse.put("message", InvalidUserEnum.INVALID_USERNAME_OR_PASSWORD.getMessage());
        failedAuthResponse.put("status",  HttpStatus.FORBIDDEN.toString());

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                failedAuthResponse
        );
    }

    public static void successfulRefreshTokenResponse(HttpServletResponse response,
                                                 String accessToken,
                                                 String refreshToken,
                                                 String info) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> successTokenRefresh = new HashMap<>();
        successTokenRefresh.put("access_token", accessToken);
        successTokenRefresh.put("refresh_token", refreshToken);

        new ObjectMapper().writeValue(
                response.getOutputStream(),
                successTokenRefresh
        );
    }


}
