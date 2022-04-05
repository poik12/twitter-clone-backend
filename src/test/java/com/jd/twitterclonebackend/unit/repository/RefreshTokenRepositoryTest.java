package com.jd.twitterclonebackend.unit.repository;

import com.jd.twitterclonebackend.entity.RefreshTokenEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.repository.RefreshTokenRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenRepositoryTest extends UnitTestInitData {

    @Autowired
    private RefreshTokenRepository underTest;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName(value = "Should find present Optional<RefreshTokenEntity> by Token")
    void should_findRefreshTokenEntity_byToken() {
        // given
        UserEntity userEntity = initUserEntity();
        userRepository.save(userEntity);
        RefreshTokenEntity refreshTokenEntity = initRefreshTokenEntity(userEntity);
        underTest.save(refreshTokenEntity);

        // when
        var result = underTest.findByToken(TOKEN);

        // then
        assertAll(
                () -> {
                    assertEquals(refreshTokenEntity.getId(), result.get().getId());
                    assertEquals(refreshTokenEntity.getToken(), result.get().getToken());
                    assertEquals(refreshTokenEntity.getUser().getId(), result.get().getUser().getId());
                    assertEquals(refreshTokenEntity.getUser().getUsername(), result.get().getUser().getUsername());
                    assertEquals(refreshTokenEntity.getUser().getPassword(), result.get().getUser().getPassword());
                }
        );
    }

    @Test
    @DisplayName(value = "Should find empty Optional<RefreshTokenEntity> by Fake Token")
    void should_notFindRefreshTokenEntity_byFakeToken() {
        // given
        UserEntity userEntity = initUserEntity();
        userRepository.save(userEntity);
        RefreshTokenEntity refreshTokenEntity = initRefreshTokenEntity(userEntity);
        underTest.save(refreshTokenEntity);

        // when
        var result = underTest.findByToken(FAKE_TOKEN);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName(value = "Should delete RefreshTokenEntity by Token")
    void should_deleteRefreshTokenEntity_byToken() {
        // given
        UserEntity userEntity = initUserEntity();
        userRepository.save(userEntity);
        RefreshTokenEntity refreshTokenEntity = initRefreshTokenEntity(userEntity);
        underTest.save(refreshTokenEntity);

        // when
        underTest.deleteByToken(TOKEN);

        // then
        assertTrue(underTest.findByToken(TOKEN).isEmpty());
    }

    @Test
    @DisplayName(value = "Should delete RefreshTokenEntity by UserEntity")
    void should_deleteRefreshTokenEntity_byUserEntity() {
        // given
        UserEntity userEntity = initUserEntity();
        userRepository.save(userEntity);
        RefreshTokenEntity refreshTokenEntity = initRefreshTokenEntity(userEntity);
        underTest.save(refreshTokenEntity);

        // when
        underTest.deleteAllByUser(userEntity);

        // then
        assertTrue(underTest.findByToken(TOKEN).isEmpty());
    }

    @Test
    @DisplayName(value = "Should get RefreshTokenEntity by UserEntity")
    void should_getRefreshTokenEntity_byUserEntity() {
        // given
        UserEntity userEntity = initUserEntity();
        userRepository.save(userEntity);
        RefreshTokenEntity refreshTokenEntity = initRefreshTokenEntity(userEntity);
        underTest.save(refreshTokenEntity);

        // when
        var result = underTest.getRefreshTokenByUser(userEntity);

        // then
        assertAll(
                () -> {
                    assertEquals(refreshTokenEntity.getId(), result.getId());
                    assertEquals(refreshTokenEntity.getToken(), result.getToken());
                    assertEquals(refreshTokenEntity.getUser().getId(), result.getUser().getId());
                    assertEquals(refreshTokenEntity.getUser().getUsername(), result.getUser().getUsername());
                    assertEquals(refreshTokenEntity.getUser().getPassword(), result.getUser().getPassword());
                }
        );

    }

}