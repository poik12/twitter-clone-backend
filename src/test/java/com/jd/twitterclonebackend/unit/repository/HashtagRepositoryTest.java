package com.jd.twitterclonebackend.unit.repository;

import com.jd.twitterclonebackend.entity.HashtagEntity;
import com.jd.twitterclonebackend.repository.HashtagRepository;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HashtagRepositoryTest extends UnitTestInitData {

    @Autowired
    private HashtagRepository underTest;

    @Test
    @DisplayName(value = "Should find present Optional<HashtagEntity> by Hashtag Value")
    void should_findHashtagEntity_byValue() {
        // given
        HashtagEntity hashtagEntity = initHashtagEntity();
        underTest.save(hashtagEntity);
        String hashtagValue = hashtagEntity.getValue();

        // when
        var result = underTest.findByValue(hashtagValue);

        // then
        assertAll(
                () -> {
                    assertEquals(hashtagEntity.getId(), result.get().getId());
                    assertEquals(hashtagEntity.getValue(), result.get().getValue());
                    assertEquals(hashtagEntity.getTweets(), result.get().getTweets());
                }
        );
    }

    @Test
    @DisplayName(value = "Should find empty Optional<HashtagEntity> by False Hashtag Value")
    void should_notFindHashtagEntity_byFalseValue() {
        // given
        HashtagEntity hashtagEntity = initHashtagEntity();
        underTest.save(hashtagEntity);

        // when
        var result = underTest.findByValue(FAKE_HASHTAG_VALUE);

        // then
        assertTrue(result.isEmpty());
    }
}