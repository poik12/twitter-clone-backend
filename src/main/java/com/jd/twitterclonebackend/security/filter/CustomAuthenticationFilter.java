package com.jd.twitterclonebackend.security.filter;

import com.jd.twitterclonebackend.dto.LoginRequestDto;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.security.SecurityResponse;
import com.jd.twitterclonebackend.security.jwt.AccessTokenProvider;
import com.jd.twitterclonebackend.security.jwt.RefreshTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
                                      AccessTokenProvider accessTokenProvider,
                                      RefreshTokenProvider refreshTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;

    }

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
            throw new UserException(InvalidUserEnum.AUTHENTICATION_FAILED.getMessage() + e);
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

}
