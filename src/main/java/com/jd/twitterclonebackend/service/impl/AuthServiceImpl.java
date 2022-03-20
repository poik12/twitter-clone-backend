package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.config.security.filter.jwt.RefreshTokenProvider;
import com.jd.twitterclonebackend.dto.request.RefreshTokenRequestDto;
import com.jd.twitterclonebackend.dto.request.RegisterRequestDto;
import com.jd.twitterclonebackend.dto.response.AuthResponseDto;
import com.jd.twitterclonebackend.dto.response.EmailConfirmationDto;
import com.jd.twitterclonebackend.dto.response.EmailNotificationDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.VerificationTokenEntity;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.mapper.AuthMapper;
import com.jd.twitterclonebackend.repository.RefreshTokenRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.repository.VerificationTokenRepository;
import com.jd.twitterclonebackend.service.AuthService;
import com.jd.twitterclonebackend.service.MailService;
import com.jd.twitterclonebackend.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;
    private final UserDetailsServiceImpl userDetailsService;
    private final VerificationTokenService verificationTokenService;

    private final AuthMapper authMapper;

    private final RefreshTokenProvider refreshTokenProvider;

    // Create User account
    @Override
    public UserEntity createUserAccount(RegisterRequestDto registerDto) {
        // Check if user exists in database
        validateIfUserExists(registerDto);
        // Map Register Dto to User Entity
        UserEntity userEntity = authMapper.mapFromDtoToEntity(registerDto);
        // Save User in Repository
        userRepository.save(userEntity);
        // Generate verification token for created User
        String token = verificationTokenService.generateVerificationToken(userEntity);
        // Create activation email with generated token for created user
        EmailNotificationDto activationEmail = mailService.createActivationEmail(
                userEntity,
                token
        );
        // Send activation email with generated token to created user
        mailService.sendEmail(activationEmail);
        return userEntity;
    }

    // Validate User
    private void validateIfUserExists(RegisterRequestDto registerDto) {
        // Check if user with username currently exists in db, if exists throw exception
        userRepository
                .findByUsername(registerDto.getUsername())
                .ifPresent((userEntity) -> {
                    throw new UserException(
                            InvalidUserEnum.USER_ALREADY_EXISTS_WITH_USERNAME.getMessage() + userEntity.getUsername(),
                            HttpStatus.BAD_REQUEST
                    );
                });
        // Check if user with email address currently exists in db, if exists throw exception
        userRepository
                .findByEmailAddress(registerDto.getEmailAddress())
                .ifPresent((userEntity) -> {
                    throw new UserException(
                            InvalidUserEnum.USER_ALREADY_EXISTS_WITH_EMAIL.getMessage() + userEntity.getEmailAddress(),
                            HttpStatus.BAD_REQUEST
                    );
                });
    }

    // Confirm user account by email
    @Override
    public EmailConfirmationDto confirmUserAccount(String token) {
        // Validate verification token
        VerificationTokenEntity verificationTokenEntity = verificationTokenService.validateVerificationToken(token);
        // Validate User with verification token
        verificationTokenService.validateUserByVerificationToken(verificationTokenEntity);
        // Update token confirmation in db
        verificationTokenService.updateVerificationToken(verificationTokenEntity);
        // Return info about user account confirmation
        return EmailConfirmationDto.builder()
                .message("Email has been confirmed")
                .build();
    }

    // Delete account
    @Override
    @Transactional
    public void deleteUserAccount() {
        // Get currently logged user
        UserEntity userEntity = userDetailsService.currentLoggedUserEntity();
        // Delete refresh token, verification token and user account
        refreshTokenRepository.deleteAllByUser(userEntity);
        verificationTokenRepository.deleteAllByUser(userEntity);
        userRepository.delete(userEntity);
    }

    // Refresh Access Token
    @Override
    public AuthResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        return refreshTokenProvider.refreshAccessToken(refreshTokenRequestDto);
    }

}
