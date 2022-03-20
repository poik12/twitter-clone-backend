package com.jd.twitterclonebackend.config.security.filter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
abstract class JwtProvider {

    // JWT PROPERTIES
    private static final Instant EXPIRATION_TIME_OF_ACCESS_TOKEN = Instant.now().plus(24, ChronoUnit.HOURS);
    private static final Instant EXPIRATION_TIME_OF_REFRESH_TOKEN = Instant.now().plus(30, ChronoUnit.DAYS);
    // Create algorithm for JWT - verify Signature
    private static final String SECRET_KEY = "secret";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY.getBytes());
    // Claims
    private static final String CLAIMS_ROLE = "roles";

    // Decode JWT
    public DecodedJWT decodeJwt(String authorizationHeader) {
        // JWT verifier
        JWTVerifier jwtVerifier = JWT
                .require(getVerifySignature())
                .build();
        // Decode JWT with JWT verifier
        return jwtVerifier.verify(getTokenFromRequestHeader(authorizationHeader));
    }

    // Remove "Bearer " from JWT
    protected String getTokenFromRequestHeader(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }

    protected Algorithm getVerifySignature() {
        return ALGORITHM;
    }

    public String getRoleClaims() {
        return CLAIMS_ROLE;
    }

    protected Instant getExpirationTimeOfAccessToken() {
        return EXPIRATION_TIME_OF_ACCESS_TOKEN;
    }

    protected Instant getExpirationTimeOfRefreshToken() {
        return EXPIRATION_TIME_OF_REFRESH_TOKEN;
    }



}
