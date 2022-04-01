package com.jd.twitterclonebackend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.controller.CommentController;
import com.jd.twitterclonebackend.controller.handler.CommentControllerExceptionHandler;
import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import com.jd.twitterclonebackend.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends IntegrationTestInitData {

    public MockMvc mockMvc;

    @Autowired
    public CommentController commentController;

    @MockBean
    public CommentService commentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new CommentControllerExceptionHandler())
                .build();
    }

    @Test
    @DisplayName(value = "Should add new Comment by Comment Request Dto")
    void should_addComment() throws Exception {
        // given
        CommentRequestDto commentRequestDto = initCommentRequestDto();

        // when then
        mockMvc.perform(
                        post("/comments")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsBytes(commentRequestDto))
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName(value = "Should get List<CommentResponseDto> by Tweet Id")
    void should_getCommentResponseDtoList_byTweetId() throws Exception {
        // given
        CommentResponseDto commentResponseDto = initCommentResponseDto();

        List<CommentResponseDto> commentResponseDtoList = List.of(
                commentResponseDto,
                commentResponseDto,
                commentResponseDto
        );

        // when then
        when(commentService.getAllCommentsForPost(any(), any()))
                .thenReturn(commentResponseDtoList);

        mockMvc.perform(
                        get("/comments/by-tweet/{tweetId}", commentResponseDto.getTweetId())
                                .param("pageNumber", "0")
                                .param("pageSize", "10")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].username").value(commentResponseDto.getUsername()))
                .andExpect(jsonPath("$[0].text").value(commentResponseDto.getText()));

    }

    @Test
    @DisplayName(value = "Should get List<CommentResponseDto> by Username")
    void should_getCommentResponseDtoList_byUsername() throws Exception {
        // given
        CommentResponseDto commentResponseDto = initCommentResponseDto();

        List<CommentResponseDto> commentResponseDtoList = List.of(
                commentResponseDto,
                commentResponseDto,
                commentResponseDto
        );

        // when then
        when(commentService.getThreeLastCommentsForPostByUsernameAndPostId(any(), any(), any()))
                .thenReturn(commentResponseDtoList);

        mockMvc.perform(
                        get("/comments/by-user/{username}/{tweetId}",
                                commentResponseDto.getUsername(), commentResponseDto.getTweetId())
                                .param("pageNumber", "0")
                                .param("pageSize", "10")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].username").value(commentResponseDto.getUsername()))
                .andExpect(jsonPath("$[0].text").value(commentResponseDto.getText()));

    }

    @Test
    @DisplayName(value = "Should delete Comment Entity by Comment Id")
    void should_deleteComment_byId() throws Exception {
        // given
        CommentResponseDto commentResponseDto = initCommentResponseDto();

        // when then
        mockMvc.perform(
                        delete("/comments/{commentId}", commentResponseDto.getTweetId())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

}