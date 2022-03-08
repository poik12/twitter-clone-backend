package com.jd.twitterclonebackend.service;

import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.VerificationTokenEntity;

import java.time.Instant;

public interface VerificationTokenService {

    String generateVerificationToken(UserEntity userEntity);

    VerificationTokenEntity validateVerificationToken(String token);

    void validateUserByVerificationToken(VerificationTokenEntity verificationTokenEntity);

    void updateVerificationToken(String token, Instant time);
}
