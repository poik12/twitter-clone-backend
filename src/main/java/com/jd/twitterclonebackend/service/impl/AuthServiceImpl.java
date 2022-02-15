package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.enums.UserRole;
import com.jd.twitterclonebackend.domain.VerificationTokenEntity;
import com.jd.twitterclonebackend.dto.*;
import com.jd.twitterclonebackend.enums.InvalidAuthenticationEnum;
import com.jd.twitterclonebackend.enums.InvalidTokenEnum;
import com.jd.twitterclonebackend.exception.TokenException;
import com.jd.twitterclonebackend.exception.UserAlreadyExistsException;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.mapper.AuthMapper;
import com.jd.twitterclonebackend.repository.RefreshTokenRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.repository.VerificationTokenRepository;
import com.jd.twitterclonebackend.security.jwt.RefreshTokenProvider;
import com.jd.twitterclonebackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    public UserEntity createUserAccount(RegisterRequestDto registerRequestDto) {
        // Check if user exists in database
        validateIfUserExists(registerRequestDto);
        // Map User Dto to User Entity
        UserEntity userEntity = authMapper.mapFromDtoToEntity(registerRequestDto);
        // Save User in Repository
        log.info("Saving new user {} to the database", registerRequestDto.getName());
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
    private void validateIfUserExists(RegisterRequestDto registerRequestDto) {
        // Check if user with username currently exists in db, if exists throw exception
        userRepository
                .findByUsername(registerRequestDto.getUsername())
                .ifPresent((userEntity) -> {
                    throw new UserAlreadyExistsException(
                            InvalidAuthenticationEnum.USER_ALREADY_EXISTS,
                            userEntity.getUsername()
                    );
                });

        // Check if user with email address currently exists in db, if exists throw exception
        userRepository
                .findByEmailAddress(registerRequestDto.getEmailAddress())
                .ifPresent((userEntity) -> {
                    throw new UserAlreadyExistsException(
                            InvalidAuthenticationEnum.USER_ALREADY_EXISTS,
                            userEntity.getEmailAddress()
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
        // Validate verification token
        VerificationTokenEntity verificationTokenEntityInDb = validateVerificationToken(token);
        // Validate User with verification token
        validateUserByVerificationToken(verificationTokenEntityInDb);
        // Update token confirmation in db
        verificationTokenRepository.updateConfirmedAt(
                token,
                Instant.now()
        );
        return InvalidAuthenticationEnum.EMAIL_CONFIRMED.getMessage();
    }

    private void validateUserByVerificationToken(VerificationTokenEntity verificationTokenEntityInDb) {
        // Find user to enable in db
        UserEntity user = userRepository
                .findByUsername(verificationTokenEntityInDb.getUser().getUsername())
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + verificationTokenEntityInDb.getUser().getUsername()
                ));
        // Check if user is enabled
        if (user.getEnabled()) {
            throw new TokenException(InvalidTokenEnum.USER_ALREADY_CONFIRMED.getMessage());
        }
        // Enable user and save in repository
        user.setEnabled(true);
        userRepository.save(user);
    }

    @NotNull
    private VerificationTokenEntity validateVerificationToken(String token) {
        // Find token in db
        VerificationTokenEntity verificationTokenEntityInDb = verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new TokenException(InvalidTokenEnum.INVALID_VERIFICATION_TOKEN.getMessage()));
        // Check if token isn't confirmed
        if (verificationTokenEntityInDb.getConfirmedAt() != null) {
            throw new TokenException(InvalidTokenEnum.EMAIL_ALREADY_CONFIRMED.getMessage());
        }
        return verificationTokenEntityInDb;
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
    public AuthResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        return refreshTokenProvider.refreshAccessToken(refreshTokenRequestDto);
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
