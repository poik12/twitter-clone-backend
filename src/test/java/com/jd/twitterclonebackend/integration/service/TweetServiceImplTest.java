package com.jd.twitterclonebackend.integration.service;

import com.jd.twitterclonebackend.dto.request.TweetRequestDto;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.exception.TweetException;
import com.jd.twitterclonebackend.exception.enums.InvalidTweetEnum;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

class TweetServiceImplTest extends IntegrationTestInitData {

    @BeforeEach
    void setUp() {
        initCurrentLoggedUser();
    }

    @Test
    @DisplayName(value = "Should add new Tween Entity without Image File")
    void should_addTweet_withoutFile() {
        // given
        TweetRequestDto tweetRequestDto = initTweetRequestDto();
        String tweetRequestJson = initRequestDtoAsJson(tweetRequestDto);

        // when
        tweetService.addTweet(null, tweetRequestJson);

        // then
        assertAll(
                () -> {
                    assertThat(fileRepository.findAll()).hasSize(0);
                    assertThat(tweetRepository.findAll()).hasSize(1);
                    TweetEntity tweetEntity = tweetRepository.findByDescription(tweetRequestDto.getDescription());
                    assertThat(tweetEntity.getDescription()).isEqualTo(tweetRequestDto.getDescription());
                }
        );
    }

    @Test
    @DisplayName(value = "Should add new Tween Entity with Image Files")
    void should_addTweet_withFile() {
        // given
        TweetRequestDto tweetRequestDto = initTweetRequestDto();
        String tweetRequestJson = initRequestDtoAsJson(tweetRequestDto);
        MockMultipartFile[] files = initMultiPartFiles();
        assertThat(files).hasSize(2);

        // when
        tweetService.addTweet(files, tweetRequestJson);

        // then
        assertAll(
                () -> {
                    assertThat(fileRepository.findAll()).hasSize(2);
                    assertThat(tweetRepository.findAll()).hasSize(1);
                    TweetEntity tweetEntity = tweetRepository.findByDescription(tweetRequestDto.getDescription());
                    assertThat(tweetEntity.getDescription()).isEqualTo(tweetRequestDto.getDescription());
                }
        );
    }

    // TODO: test doesn't work properly
    @Test
    @DisplayName(value = "Should get List<TweetResponseDto>")
    void should_getAllTweets_sortedByTimeStamp() {
        // given
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.DESC,
                "createdAt"
        );

        List<TweetEntity> tweetEntities = initTweetListInDatabase();
        System.out.println(tweetEntities);
        assertThat(tweetEntities.size()).isEqualTo(1);

        // when
        var result = tweetService.getAllTweets(pageable);
        System.out.println(result);
        // then
//        assertAll(
//                () -> {
//                    assertThat(result.size()).isEqualTo(3);
////                    assertThat(result.size()).isEqualTo(tweetEntityList.size());
////                    assertEquals(result.get(0).getFileContent().get(0),
////                            tweetEntityList.get(0).getImages().get(0).getContent());
//                }
//        );
    }


    @Test
    @DisplayName(value = "Should get Tweet Response Dto by Tweet Id")
    void should_getTweetDto_byTweetId() {
        // given
        TweetEntity tweetEntity = initTweetInDatabase();

        // when
        var response =
                tweetService.getTweetById(tweetEntity.getId());

        // then
        assertAll(
                () -> {
                    assertThat(response.getId()).isEqualTo(tweetEntity.getId());
                    assertThat(response.getName()).isEqualTo(tweetEntity.getUser().getName());
                    assertThat(response.getUsername()).isEqualTo(tweetEntity.getUser().getUsername());
                    assertThat(response.getDescription()).isEqualTo(tweetEntity.getDescription());
                    assertThat(response.getCommentNo()).isEqualTo(tweetEntity.getCommentNo());
                    assertThat(response.getUserProfilePicture())
                            .isEqualTo(tweetEntity.getUser().getProfilePicture());
                }
        );
    }

    @Test
    @DisplayName(value = "Should throw Tweet Exception when Tweet Entity was not found by Id")
    void should_throwTweetException_whenTweetWasNotFoundById() {
        // given
        List<TweetEntity> tweetEntityList = initTweetListInDatabase();
        assertThat(tweetEntityList).hasSize(1);
        long wrongTweetId = 100L;

        // when
        var result = assertThrows(
                TweetException.class,
                () -> tweetService.getTweetById((wrongTweetId))
        );

        // then
        assertAll(
                () -> {
                    assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(result.getMessage())
                            .isEqualTo(InvalidTweetEnum.TWEET_NOT_FOUND_WITH_ID.getMessage() + wrongTweetId);
                }
        );
    }

    @Test
    @DisplayName(value = "Should get List<TweetResponseDto> by Username")
    void should_getTweets_byUsername() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName(value = "Should delete Tweet Entity by Tweet Id")
    void should_deleteTweetEntity_byId() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName(value = "Should like Tweet Entity by Tweet Id")
    void should_likeTweetEntity_byId() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName(value = "Should get liked List<TweetResponseDto> by Username")
    void should_getLikedTweets_byUsername() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName(value = "Should get List<RepliedTweetResponseDto> by Username")
    void should_getRepliedTweetsWithComments_byUsername() {
        // given

        // when

        // then

    }

}