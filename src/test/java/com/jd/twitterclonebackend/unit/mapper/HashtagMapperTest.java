package com.jd.twitterclonebackend.unit.mapper;

import com.jd.twitterclonebackend.entity.HashtagEntity;
import com.jd.twitterclonebackend.mapper.HashtagMapper;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HashtagMapperTest extends UnitTestInitData {

    @InjectMocks
    private HashtagMapper hashtagMapper;

    @Test
    @DisplayName(value = "Should map from Hashtag String to Hashtag Entity")
    void should_mapFromHashtagDtoToEntity() {
        // given
        String hashtag = "#RandomHashtag";

        // when
        var result = hashtagMapper.mapFromDtoToEntity(hashtag);

        // then
        assertAll(
                () -> {
                    assertEquals(hashtag, result.getValue());
                }
        );

    }

    @Test
    @DisplayName(value = "Should map from List<HashtagEntity> to List<String> hashtag values")
    void should_mapFromHashtagEntity_toStringList() {
        // given
        List<HashtagEntity> hashtagEntityList = List.of(initHashtagEntity());

        // when
        var result = hashtagMapper.mapFromEntityToStringList(hashtagEntityList);

        // then
        assertAll(
                () -> {
                    assertThat(result.size()).isEqualTo(hashtagEntityList.size());
                    assertThat(result.get(0)).isEqualTo(hashtagEntityList.get(0).getValue());
                }
        );

    }
}