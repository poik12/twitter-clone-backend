package com.jd.twitterclonebackend.config.security.filter;

import com.jd.twitterclonebackend.config.security.SecurityResponse;
import com.jd.twitterclonebackend.config.security.filter.jwt.AccessTokenProvider;
import com.jd.twitterclonebackend.dto.request.LoginRequestDto;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.config.security.filter.jwt.RefreshTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    // Login authentication
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            // Get authentication request from login request
            LoginRequestDto authenticationRequest = new ObjectMapper().readValue(
                    request.getInputStream(),
                    LoginRequestDto.class
            );
            // Generate authentication token
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );
            // Authenticate in AuthenticationManager
            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            throw new UserException(InvalidUserEnum.AUTHENTICATION_FAILED.getMessage() + e, HttpStatus.BAD_REQUEST);
        }
    }

    // Successful login
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        // Get user that was successfully authenticated
        User user = (User) authentication.getPrincipal();
        // Create access token for that user
        String accessToken = accessTokenProvider.createAccessTokenForPrincipal(user);
        // Get access token expiation time
        Instant accessTokenExpirationTime = accessTokenProvider.getAccessTokenExpirationTime();
        // Create refresh token for user
        String refreshToken = refreshTokenProvider.createRefreshTokenForPrincipal(user);
        // Create response for user
        SecurityResponse.successfulAuthenticationResponse(
                response,
                accessToken,
                accessTokenExpirationTime,
                refreshToken,
                user.getUsername()
        );
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.error("Error logging in: {}", failed.getMessage());
        SecurityResponse.failedAuthenticationResponse(
                response,
                failed,
                "Authorization error");
    }
}
