package com.jd.twitterclonebackend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.controller.TweetController;
import com.jd.twitterclonebackend.controller.handler.TweetControllerExceptionHandler;
import com.jd.twitterclonebackend.dto.request.TweetRequestDto;
import com.jd.twitterclonebackend.dto.response.TweetResponseDto;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import com.jd.twitterclonebackend.service.TweetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class TweetControllerTest extends IntegrationTestInitData {

    public MockMvc mockMvc;

    @Autowired
    public TweetController tweetController;

    @MockBean
    public TweetService tweetService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tweetController)
                .setControllerAdvice(new TweetControllerExceptionHandler())
                .build();
    }

    @Test
    void should_addPostRequestWithFile() throws Exception {
        // given
        TweetRequestDto tweetRequestDto = initPostRequestDto();
        String postRequestJson = initRequestDtoAsJson(tweetRequestDto);
        MultipartFile file = new MockMultipartFile("file", "file".getBytes());


        // when then
        mockMvc.perform(
                        multipart("/posts")
                                .file("file", file.getBytes())
                                .param("postRequest", postRequestJson)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void should_getPostResponseDtoList() throws Exception {
        // given
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.DESC,
                "createdAt"
        );


        TweetResponseDto tweetResponseDto = initPostResponseDto();
        List<TweetResponseDto> tweetResponseDtoList = List.of(
                tweetResponseDto,
                tweetResponseDto,
                tweetResponseDto
        );

        // when then
        when(tweetService.getAllTweets(pageable)).thenReturn(tweetResponseDtoList);

        mockMvc.perform(
                        get("/posts")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(tweetResponseDto.getId()))
                .andExpect(jsonPath("$[0].name").value(tweetResponseDto.getName()));
    }

    @Test
    void should_getPostResponseDto_byPostId() throws Exception {
        // given
        TweetResponseDto tweetResponseDto = initPostResponseDto();

        // when then
        when(tweetService.getTweetById(any())).thenReturn(tweetResponseDto);

        mockMvc.perform(
                        get("/posts/{postId}", tweetResponseDto.getId())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tweetResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(tweetResponseDto.getName()));

    }

    @Test
    void should_getPostResponseDtoList_byUsername() throws Exception {
        // given
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.DESC,
                "createdAt"
        );

        TweetResponseDto tweetResponseDto = initPostResponseDto();

        List<TweetResponseDto> tweetResponseDtoList = List.of(
                tweetResponseDto,
                tweetResponseDto,
                tweetResponseDto
        );

        // when then
        when(tweetService.getTweetsByUsername(any(), pageable)).thenReturn(tweetResponseDtoList);

        mockMvc.perform(
                        get("/posts/by-user/{username}", tweetResponseDto.getUsername())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(tweetResponseDto.getId()))
                .andExpect(jsonPath("$[0].name").value(tweetResponseDto.getName()));
    }

    @Test
    void should_deletePost_byId() throws Exception {
        // given
        TweetResponseDto tweetResponseDto = initPostResponseDto();

        // when then
        mockMvc.perform(
                        delete("/posts/{postId}", tweetResponseDto.getId())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}