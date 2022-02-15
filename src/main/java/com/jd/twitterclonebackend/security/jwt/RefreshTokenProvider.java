package com.jd.twitterclonebackend.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jd.twitterclonebackend.domain.RefreshTokenEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.AuthResponse;
import com.jd.twitterclonebackend.dto.RefreshTokenRequest;
import com.jd.twitterclonebackend.enums.InvalidTokenEnum;
import com.jd.twitterclonebackend.exception.TokenException;
import com.jd.twitterclonebackend.repository.RefreshTokenRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RefreshTokenProvider extends JwtProvider {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenProvider accessTokenProvider;

    // Create new refresh token
    @Transactional
    public String createRefreshTokenForPrincipal(User user) {
        // Find currently logged user in repository
        UserEntity userEntity = userRepository
                .findByUsername(user.getUsername())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with username" + user.getUsername() + " not found.")
                );

        // Check if user already has refresh token in repository
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.getRefreshTokenByUser(userEntity);

        // If user has refresh token and is not expired, return that token
        if (Objects.nonNull(userEntity)
                && Objects.nonNull(refreshTokenEntity)
                && refreshTokenEntity.getExpiresAt().isAfter(Instant.now())) {

            return refreshTokenEntity.getToken();

        } else if (Objects.nonNull(userEntity)
                && Objects.nonNull(refreshTokenEntity)
                && !refreshTokenEntity.getExpiresAt().isAfter(Instant.now())) {

            // Delete old refresh token
            refreshTokenRepository.delete(refreshTokenEntity);

            // Create refresh token as JWT
            String refreshToken = generateRefreshToken(user);

            RefreshTokenEntity newRefreshTokenEntity = createNewRefreshTokenEntity(
                    userEntity,
                    refreshToken
            );

            return newRefreshTokenEntity.getToken();

        } else {
            // Create refresh token as JWT
            String refreshToken = generateRefreshToken(user);

            // Create new refresh token entity
            RefreshTokenEntity newRefreshTokenEntity = createNewRefreshTokenEntity(
                    userEntity,
                    refreshToken
            );

            return newRefreshTokenEntity.getToken();
        }
    }

    private RefreshTokenEntity createNewRefreshTokenEntity(UserEntity userEntity, String refreshToken) {
        // Create new refresh token entity
        RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity(
                refreshToken,
                Instant.now(),
                EXPIRATION_TIME_OF_REFRESH_TOKEN,
                userEntity
        );

        // Save created JWT in database
        refreshTokenRepository.save(newRefreshTokenEntity);

        return newRefreshTokenEntity;
    }


    private String generateRefreshToken(User user) {
        // Create JWT token
        return JWT.create()
                .withSubject(user.getUsername()) // Header
                .withIssuer("Refresh Token")
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(Date.from(EXPIRATION_TIME_OF_REFRESH_TOKEN))
                .sign(ALGORITHM);
    }

    // Validate refresh token
    public RefreshTokenEntity validateRefreshToken(String refreshToken) {
        // Find refresh token in database
        RefreshTokenEntity refreshTokenEntityFromDb = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new TokenException(InvalidTokenEnum.INVALID_REFRESH_TOKEN.getMessage()));
        // Check if refresh token has not expired
        if (refreshTokenEntityFromDb.getExpiresAt().isBefore(Instant.now())) {
            throw new TokenException(InvalidTokenEnum.REFRESH_TOKEN_EXPIRED.getMessage());
        }
        return refreshTokenEntityFromDb;
    }

    // Delete refresh token from repository
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    // Refresh access token
    public AuthResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {



        // Check if refresh Token is valid
        RefreshTokenEntity refreshTokenEntity = validateRefreshToken(refreshTokenRequest.getRefreshToken());
        // Decode JWT(Remove Bearer from JWT, Verify algorithm signature)
        DecodedJWT decodedJWT = decodeJwt("Bearer " + refreshTokenRequest.getRefreshToken());
        // Get username from decoded JWT
        String username = decodedJWT.getSubject();
        // Check if user exists in database
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        // Generate new access token for user
        String accessToken = accessTokenProvider.refreshAccessTokenUserEntity(userEntity);
        // Create response for user
        return AuthResponse.builder()
                .username(username)
                .authenticationToken(accessToken)
                .expiresAt(String.valueOf(EXPIRATION_TIME_OF_ACCESS_TOKEN))
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .build();
    }


//    // Refresh access token
//    public void refreshAccessToken(HttpServletRequest request,
//                                   HttpServletResponse response) throws IOException {
//
//        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            try {
//                // Get back refresh token from authorization header
//                String refreshToken = getTokenFromRequestHeader(authorizationHeader);
//                // Check if refresh Token is valid
//                RefreshTokenEntity refreshTokenEntity = validateRefreshToken(refreshToken);
//                // Decode JWT(Remove Bearer from JWT, Verify algorithm signature)
//                DecodedJWT decodedJWT = decodeJwt(authorizationHeader);
//                // Get username from decoded JWT
//                String username = decodedJWT.getSubject();
//                // Check if user exists in database
//                UserEntity userEntity = userRepository
//                        .findByUsername(username)
//                        .orElseThrow(() -> new UsernameNotFoundException(username));
//                // Generate new access token for user
//                String accessToken = accessTokenProvider.refreshAccessTokenUserEntity(userEntity);
//
////                // Create response for user
////                new SecurityResponse().successfulRefreshTokenResponse(
////                        response,
////                        accessToken,
////                        refreshToken,
////                        "new_access_token"
////                );
//
//                // Create response for user
//                new SecurityResponse().successfulAuthenticationResponse(
//                        response,
//                        accessToken,
//                        refreshTokenEntity,
//                        userEntity.getUsername()
//                );
//
//            } catch (Exception exception) {
//                // Create response for user
//                new SecurityResponse().failedAuthorizationResponse(
//                        response,
//                        exception,
//                        "failed refresh access token");
//            }
//        } else {
//            throw new InvalidTokenException(InvalidTokenEnum.MISSING_REFRESH_TOKEN.getMessage());
//        }
//    }
}
