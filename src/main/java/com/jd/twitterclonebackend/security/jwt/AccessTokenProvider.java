package com.jd.twitterclonebackend.security.jwt;

import com.auth0.jwt.JWT;
import com.jd.twitterclonebackend.domain.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessTokenProvider extends JwtProvider{

    public String createAccessTokenForPrincipal(User user) {
        // Get user authorities for JWT
        List<String> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        // Create JWT - header, payload and verify signature
        return JWT.create()
                .withSubject(user.getUsername()) // Header
                .withClaim(CLAIMS_ROLE, authorities) // Payload
                .withIssuer("Access Token")
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(EXPIRATION_TIME_OF_ACCESS_TOKEN))
                .sign(ALGORITHM); // Verify Signature
    }

    protected String refreshAccessTokenUserEntity(UserEntity userEntity) {
        // Get user roles for JWT
        List<String> roles = List.of(userEntity.getUserRole().name());
        // Create JWT - header, payload and verify signature
        return JWT.create()
                .withSubject(userEntity.getUsername()) // Header
                .withClaim(CLAIMS_ROLE, roles) // Payload
                .withIssuer("Access Token")
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(EXPIRATION_TIME_OF_ACCESS_TOKEN))
                .sign(ALGORITHM); // Verify Signature
    }

    public Instant getAccessTokenExpirationTime() {
        return EXPIRATION_TIME_OF_ACCESS_TOKEN;
    }
}
