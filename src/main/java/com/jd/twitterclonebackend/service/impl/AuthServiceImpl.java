package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.domain.UserRole;
import com.jd.twitterclonebackend.domain.VerificationTokenEntity;
import com.jd.twitterclonebackend.dto.*;
import com.jd.twitterclonebackend.exception.InvalidTokenEnum;
import com.jd.twitterclonebackend.exception.InvalidTokenException;
import com.jd.twitterclonebackend.exception.UserAlreadyExistsException;
import com.jd.twitterclonebackend.mapper.AuthMapper;
import com.jd.twitterclonebackend.repository.RefreshTokenRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.repository.VerificationTokenRepository;
import com.jd.twitterclonebackend.security.jwt.RefreshTokenProvider;
import com.jd.twitterclonebackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final AuthMapper authMapper;

    private final MailServiceImpl mailService;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenProvider refreshTokenProvider;

    // Create User in Db
    @Override
    public UserEntity createUserAccount(RegisterRequest registerRequest) {
        // Check if user exists in database
        validateIfUserExists(registerRequest);
        // Map User Dto to User Entity
        log.info("Saving new user {} to the database", registerRequest.getName());
        UserEntity userEntity = authMapper.mapFromDtoToEntity(registerRequest);
        // Save User in Repository
        userRepository.save(userEntity);
        // Generate verification token for created User
        String token = generateVerificationToken(userEntity);
        // Create activation email with generated token for created user
        NotificationEmailDto activationEmail = mailService.createActivationEmailForUser(
                userEntity,
                token
        );
        // Send activation email with generated token to created user
        mailService.sendEmailToUser(activationEmail);

        return userEntity;
    }

    // Validate User
    private void validateIfUserExists(RegisterRequest registerRequest) {
        userRepository
                .findByUsername(registerRequest.getUsername())
                .ifPresent((userEntity) -> {
                    throw new UserAlreadyExistsException(
                            "User with username: " + userEntity.getUsername() + " already exists"
                    );
                });

        userRepository
                .findByEmailAddress(registerRequest.getEmailAddress())
                .ifPresent((userEntity) -> {
                    throw new UserAlreadyExistsException(
                            "User with email address: " + userEntity.getEmailAddress() + " already exists"
                    );
                });
    }

    private String generateVerificationToken(UserEntity userEntity) {
        // Generate random token
        String randomGeneratedToken = UUID.randomUUID().toString();
        // Create verification token object
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity(
                randomGeneratedToken,
                Instant.now(),
                Instant.now().plus(15, ChronoUnit.MINUTES),
                userEntity
        );
        // Save verification token in database
        verificationTokenRepository.save(verificationTokenEntity);
        // Return generated token to user in mail
        return randomGeneratedToken;
    }

    // Confirm user account by email
    @Override
    @Transactional
    public String confirmUserAccount(String token) {
        // Find token in db
        VerificationTokenEntity verificationTokenEntityInDb = verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(InvalidTokenEnum.INVALID_VERIFICATION_TOKEN.getMessage()));
        // Check if token isn't confirmed
        if (verificationTokenEntityInDb.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }
        // Find user to enable in db
        UserEntity user = userRepository
                .findByUsername(verificationTokenEntityInDb.getUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username: " + verificationTokenEntityInDb.getUser().getUsername() + " does not exist"
                ));
        // Check if user is enabled
        if (user.getEnabled()) {
            throw new RuntimeException("User is already confirmed");
        }
        // Enable user and save in repository
        user.setEnabled(true);
        userRepository.save(user);

        // Update token confirmation
        verificationTokenRepository.updateConfirmedAt(
                token,
                Instant.now()
        );

        return "Email has been confirmed";
    }

//    TODO: delete account
    // Delete account
    @Override
    @Transactional
    public void deleteUserAccount() {
        // Get currently logged user
        UserEntity userEntity = userDetailsService.currentLoggedUserEntity();
        // Delete refresh token, verification token and user entity
        refreshTokenRepository.deleteAllByUser(userEntity);
        verificationTokenRepository.deleteAllByUser(userEntity);
        userRepository.delete(userEntity);
        // Delete posts and comments by user
    }

    // Refresh Access Token
    @Override
    public AuthResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenProvider.refreshAccessToken(refreshTokenRequest);
    }

    // TODO: doesnt work
    @Override
    public void changeUserRole(String username, UserRole userRole) {
        log.info("Adding role {} to user {}", userRole, username);
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        userEntity.setUserRole(userRole);

        userRepository.save(userEntity);
    }


}
