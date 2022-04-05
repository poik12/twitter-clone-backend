package com.jd.twitterclonebackend.unit.repository;

import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.VerificationTokenEntity;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.repository.VerificationTokenRepository;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VerificationTokenRepositoryTest extends UnitTestInitData {

    @Autowired
    private VerificationTokenRepository underTest;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName(value = "Should find present Optional<VerificationTokenEntity> by Token")
    void should_findVerificationTokenEntity_byToken() {
        // given
        UserEntity userEntity = initUserEntity();
        userRepository.save(userEntity);
        VerificationTokenEntity verificationTokenEntity = initVerificationTokenEntity(userEntity);
        underTest.save(verificationTokenEntity);

        // when
        var result = underTest.findByToken(TOKEN);

        // then
        assertAll(
                () -> {
                    assertEquals(verificationTokenEntity.getId(), result.get().getId());
                    assertEquals(verificationTokenEntity.getToken(), result.get().getToken());
                    assertEquals(verificationTokenEntity.getUser().getId(), result.get().getUser().getId());
                    assertEquals(verificationTokenEntity.getUser().getUsername(), result.get().getUser().getUsername());
                    assertEquals(verificationTokenEntity.getUser().getPassword(), result.get().getUser().getPassword());
                }
        );

    }

    @Test
    @DisplayName(value = "Should find empty Optional<VerificationTokenEntity> by Fake Token")
    void should_notFindVerificationTokenEntity_byFakeToken() {
        // given
        UserEntity userEntity = initUserEntity();
        userRepository.save(userEntity);
        VerificationTokenEntity verificationTokenEntity = initVerificationTokenEntity(userEntity);
        underTest.save(verificationTokenEntity);

        // when
        var result = underTest.findByToken(FAKE_TOKEN);

        // then
        assertTrue(result.isEmpty());

    }

    @Test
    @DisplayName(value = "Should confirm VerificationTokenEntity by Token")
    void should_confirmVerificationTokenEntity_byToken() {
        // given
        UserEntity userEntity = initUserEntity();
        userRepository.save(userEntity);
        VerificationTokenEntity verificationTokenEntity = initVerificationTokenEntity(userEntity);
        underTest.save(verificationTokenEntity);

        // when
        underTest.updateConfirmedAt(TOKEN, TOKEN_CONFIRMATION_TIME);

        // then
        Instant confirmedAt = underTest.findByToken(TOKEN).get().getConfirmedAt();

        assertAll(
                () -> {
                    assertNotNull(confirmedAt);
                    assertThat(confirmedAt).isEqualTo(TOKEN_CONFIRMATION_TIME);
                }
        );

    }

    @Test
    @DisplayName(value = "Should delete VerificationTokenEntity by UserEntity")
    void should_deleteVerificationTokenEntity_byUserEntity() {
        // given
        UserEntity userEntity = initUserEntity();
        userRepository.save(userEntity);
        VerificationTokenEntity verificationTokenEntity = initVerificationTokenEntity(userEntity);
        underTest.save(verificationTokenEntity);

        // when
        underTest.deleteAllByUser(userEntity);

        // then
        assertTrue(underTest.findByToken(TOKEN).isEmpty());
    }

}