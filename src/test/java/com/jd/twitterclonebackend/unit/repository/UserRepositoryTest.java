package com.jd.twitterclonebackend.unit.repository;

import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

class UserRepositoryTest extends UnitTestInitData {

    @Autowired
    private UserRepository underTest;

    @Test
    @DisplayName(value = "Should find present Optional<UserEntity> by Username")
    void should_findUserEntity_ByUsername() {
        // given
        UserEntity userEntity = initUserEntity();
        underTest.save(userEntity);

        // when
        var result = underTest.findByUsername( userEntity.getUsername());

        // then
        assertAll(
                () -> {
                    assertEquals(userEntity.getId(), result.get().getId());
                    assertEquals(userEntity.getUsername(), result.get().getUsername());
                    assertEquals(userEntity.getPassword(), result.get().getPassword());
                    assertEquals(userEntity.getEmailAddress(), result.get().getEmailAddress());
                }
        );
    }

    @Test
    @DisplayName(value = "Should find empty Optional<UserEntity> by False Username")
    void should_notFindUserEntity_ByFalseUsername() {
        // given
        UserEntity userEntity = initUserEntity();
        underTest.save(userEntity);

        // when
        var result = underTest.findByUsername(FAKE_USERNAME);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName(value = "Should find present Optional<UserEntity> by Email Address")
    void should_findUserEntity_byEmailAddress() {
        // given
        UserEntity userEntity = initUserEntity();
        underTest.save(userEntity);

        // when
        var result = underTest.findByEmailAddress(userEntity.getEmailAddress());

        // then
        assertAll(
                () -> {
                    assertEquals(userEntity.getId(), result.get().getId());
                    assertEquals(userEntity.getUsername(), result.get().getUsername());
                    assertEquals(userEntity.getPassword(), result.get().getPassword());
                    assertEquals(userEntity.getEmailAddress(), result.get().getEmailAddress());
                }
        );
    }

    @Test
    @DisplayName(value = "Should find empty Optional<UserEntity> by False Email Address")
    void should_notFindUserEntity_byFalseEmailAddress() {
        // given
        UserEntity userEntity = initUserEntity();
        underTest.save(userEntity);

        // when
        var result = underTest.findByEmailAddress(FAKE_EMAIL_ADDRESS);

        // then
        assertTrue(result.isEmpty());
    }

}