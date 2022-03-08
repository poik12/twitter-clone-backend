package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.VerificationTokenEntity;
import com.jd.twitterclonebackend.exception.TokenException;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidTokenEnum;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.repository.VerificationTokenRepository;
import com.jd.twitterclonebackend.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @Override
    public String generateVerificationToken(UserEntity userEntity) {
        // Generate random token
        String randomGeneratedToken = UUID.randomUUID().toString();
        // Create verification token object
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity(
                randomGeneratedToken,
                Instant.now().plus(15, ChronoUnit.MINUTES),
                userEntity
        );
        // Save verification token in database
        verificationTokenRepository.save(verificationTokenEntity);
        // Return generated token to user in mail
        return randomGeneratedToken;
    }

    // Check if verification token is already confirmed
    @Override
    public VerificationTokenEntity validateVerificationToken(String token) {
        // Find token in db
        VerificationTokenEntity verificationTokenEntity = verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new TokenException(InvalidTokenEnum.INVALID_VERIFICATION_TOKEN.getMessage()));
        // Check if token isn't confirmed
        if (verificationTokenEntity.getConfirmedAt() != null) {
            throw new TokenException(InvalidTokenEnum.EMAIL_ALREADY_CONFIRMED.getMessage());
        }
        return verificationTokenEntity;
    }

    @Override
    public void validateUserByVerificationToken(VerificationTokenEntity verificationTokenEntity) {
        // Find user to enable in db
        String username = verificationTokenEntity.getUser().getUsername();
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username
                ));
        // Check if user is enabled
        if (userEntity.getEnabled()) {
            throw new TokenException(InvalidTokenEnum.USER_ALREADY_CONFIRMED.getMessage());
        }
        // Enable user and save in repository
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
    }

    @Override
    public void updateVerificationToken(String token, Instant time) {
        verificationTokenRepository.updateConfirmedAt(
                token,
                Instant.now()
        );
    }
}
