package com.jd.twitterclonebackend.unit.mapper;

import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.FollowerMapper;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

class FollowerMapperTest extends UnitTestInitData {

    @InjectMocks
    private FollowerMapper underTest;

    @Test
    void should_mapFromUserEntity_toFollowerResponseDto() {
        // given
        UserEntity userEntity = initUserEntity();

        // when
        var result = underTest.mapFromEntityToDto(userEntity);

        // then
        assertAll(
                () -> {
                    assertEquals(userEntity.getId(), result.getId());
                    assertEquals(userEntity.getName(), result.getName());
                    assertEquals(userEntity.getUsername(), result.getUsername());
                    assertEquals(userEntity.getProfilePicture(), result.getUserProfilePicture());
                    assertEquals(userEntity.getTweets().size(), result.getTweetNo());
                }
        );
    }
}