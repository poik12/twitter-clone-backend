package com.jd.twitterclonebackend.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jd.twitterclonebackend.config.SecurityConfig;
import com.jd.twitterclonebackend.security.SecurityResponse;
import com.jd.twitterclonebackend.security.jwt.AccessTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final AccessTokenProvider accessTokenProvider;

    public CustomAuthorizationFilter(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // If user tries to sign in or get refresh token, don't do anything
        if (request.getServletPath().equals(SecurityConfig.API_VERSION + "/auth/login")) {
            filterChain.doFilter(request, response);
        }
        // Get authorization header
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Check that header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                UsernamePasswordAuthenticationToken authenticationToken =
                        getAuthenticationTokenFromAuthorizationHeader(authorizationHeader);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);

            } catch (Exception exception) {
                log.error("Error logging in: {}", exception.getMessage());
                SecurityResponse.failedAuthorizationResponse(
                        response,
                        exception,
                        "Authorization error");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthenticationTokenFromAuthorizationHeader(String authorizationHeader) {

        DecodedJWT decodedJWT = accessTokenProvider.decodeJwt(authorizationHeader);

        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT
                .getClaim(accessTokenProvider.getRoleClaims())
                .asArray(String.class);
        // Convert roles from string array to collection that extends Simple Granted Authorities
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        Arrays.stream(roles)
                .forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                });

        // Create and return authentication token
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
        );
    }
}
