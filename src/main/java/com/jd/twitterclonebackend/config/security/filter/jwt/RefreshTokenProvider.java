package com.jd.twitterclonebackend.config.security.filter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jd.twitterclonebackend.entity.RefreshTokenEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.response.AuthResponseDto;
import com.jd.twitterclonebackend.dto.request.RefreshTokenRequestDto;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidTokenEnum;
import com.jd.twitterclonebackend.exception.TokenException;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.repository.RefreshTokenRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
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
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + user.getUsername(),
                        HttpStatus.NOT_FOUND
                ));

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
                getExpirationTimeOfRefreshToken(),
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
                .withExpiresAt(Date.from(getExpirationTimeOfRefreshToken()))
                .sign(getVerifySignature());
    }

    // Delete refresh token from repository
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    // Refresh access token
    public AuthResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        // Check if refresh Token is valid
        validateRefreshToken(refreshTokenRequestDto.getRefreshToken());
        // Decode JWT(Remove Bearer from JWT, Verify algorithm signature)
        DecodedJWT decodedJWT = decodeJwt("Bearer " + refreshTokenRequestDto.getRefreshToken());
        // Get username from decoded JWT
        String username = decodedJWT.getSubject();
        // Check if user exists in database
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username,
                        HttpStatus.NOT_FOUND
                ));
        // Generate new access token for user
        String accessToken = accessTokenProvider.refreshAccessTokenForPrincipal(userEntity);
        // Create response for user
        return AuthResponseDto.builder()
                .username(username)
                .authenticationToken(accessToken)
                .expiresAt(String.valueOf(getExpirationTimeOfAccessToken()))
                .refreshToken(refreshTokenRequestDto.getRefreshToken())
                .build();
    }

    // Validate refresh token
    public void validateRefreshToken(String refreshToken) {
        // Find refresh token in database
        RefreshTokenEntity refreshTokenEntityFromDb = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new TokenException(
                        InvalidTokenEnum.INVALID_REFRESH_TOKEN.getMessage(),
                        HttpStatus.BAD_REQUEST
                ));
        // Check if refresh token has not expired
        if (refreshTokenEntityFromDb.getExpiresAt().isBefore(Instant.now())) {
            throw new TokenException(
                    InvalidTokenEnum.REFRESH_TOKEN_EXPIRED.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
