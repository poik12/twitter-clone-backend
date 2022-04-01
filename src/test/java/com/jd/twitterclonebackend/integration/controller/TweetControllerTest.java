package com.jd.twitterclonebackend.integration.controller;

import com.jd.twitterclonebackend.controller.TweetController;
import com.jd.twitterclonebackend.controller.handler.TweetControllerExceptionHandler;
import com.jd.twitterclonebackend.dto.request.TweetRequestDto;
import com.jd.twitterclonebackend.dto.response.TweetResponseDto;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import com.jd.twitterclonebackend.service.TweetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TweetControllerTest extends IntegrationTestInitData {

    public MockMvc mockMvc;

    @Autowired
    public TweetController tweetController;

    @MockBean
    public TweetService tweetService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tweetController)
                .setControllerAdvice(new TweetControllerExceptionHandler())
                .build();
    }

    @Test
    @DisplayName(value = "Should add new Tweet Entity by Tweet Entity Request")
    void should_addTweetRequestWithFile() throws Exception {
        // given
        TweetRequestDto tweetRequestDto = initTweetRequestDto();
        String tweetRequestJson = initRequestDtoAsJson(tweetRequestDto);
        MultipartFile file = new MockMultipartFile("file", "file".getBytes());


        // when then
        mockMvc.perform(
                        multipart("/tweets")
                                .file("file", file.getBytes())
                                .param("tweetRequestJson", tweetRequestJson)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName(value = "Should get List<TweetResponseDto>")
    void should_getTweetResponseDtoList() throws Exception {
        // given
        TweetResponseDto tweetResponseDto = initTweetResponseDto();
        List<TweetResponseDto> tweetResponseDtoList = List.of(
                tweetResponseDto,
                tweetResponseDto,
                tweetResponseDto
        );

        // when then
        when(tweetService.getAllTweets(any())).thenReturn(tweetResponseDtoList);

        mockMvc.perform(
                        get("/tweets")
                                .param("pageNumber", "0")
                                .param("pageSize", "10")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(tweetResponseDto.getId()))
                .andExpect(jsonPath("$[0].name").value(tweetResponseDto.getName()));
    }

    @Test
    @DisplayName(value = "Should get List<TweetResponseDto> by Tweet Id")
    void should_getTweetResponseDto_byTweetId() throws Exception {
        // given
        TweetResponseDto tweetResponseDto = initTweetResponseDto();

        // when then
        when(tweetService.getTweetById(any()))
                .thenReturn(tweetResponseDto);

        mockMvc.perform(
                        get("/tweets/{tweetId}", tweetResponseDto.getId())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tweetResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(tweetResponseDto.getName()));
    }

    @Test
    @DisplayName(value = "Should get List<TweetResponseDto> by Username")
    void should_getTweetResponseDtoList_byUsername() throws Exception {
        // given
        TweetResponseDto tweetResponseDto = initTweetResponseDto();

        List<TweetResponseDto> tweetResponseDtoList = List.of(
                tweetResponseDto,
                tweetResponseDto,
                tweetResponseDto
        );

        // when then
        when(tweetService.getTweetsByUsername(any(), any()))
                .thenReturn(tweetResponseDtoList);

        mockMvc.perform(
                        get("/tweets/by-user/{username}", tweetResponseDto.getUsername())
                                .param("pageNumber", "0")
                                .param("pageSize", "10")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(tweetResponseDto.getId()))
                .andExpect(jsonPath("$[0].name").value(tweetResponseDto.getName()));
    }

    @Test
    @DisplayName(value = "Should delete Tweet Enitty by Tweet Id")
    void should_deletePost_byId() throws Exception {
        // given
        TweetResponseDto tweetResponseDto = initTweetResponseDto();

        // when then
        mockMvc.perform(
                        delete("/tweets/{tweetId}", tweetResponseDto.getId())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}