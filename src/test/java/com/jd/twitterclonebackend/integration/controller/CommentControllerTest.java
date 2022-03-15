package com.jd.twitterclonebackend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.controller.CommentController;
import com.jd.twitterclonebackend.controller.handler.CommentControllerExceptionHandler;
import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import com.jd.twitterclonebackend.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
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
    void should_getCommentResponseDtoList_byPostId() throws Exception {
        // given
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.DESC,
                "createdAt"
        );

        CommentResponseDto commentResponseDto = initCommentResponseDto();

        List<CommentResponseDto> commentResponseDtoList = List.of(
                commentResponseDto,
                commentResponseDto,
                commentResponseDto
        );

        // when then
        when(commentService.getAllCommentsForPost(any(), pageable))
                .thenReturn(commentResponseDtoList);

        mockMvc.perform(
                        get("/comments/by-post/{postId}", commentResponseDto.getPostId())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].username").value(commentResponseDto.getUsername()))
                .andExpect(jsonPath("$[0].text").value(commentResponseDto.getText()));

    }

    @Test
    void should_getCommentResponseDtoList_byUsername() throws Exception {
        // given
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.DESC,
                "createdAt"
        );

        CommentResponseDto commentResponseDto = initCommentResponseDto();

        List<CommentResponseDto> commentResponseDtoList = List.of(
                commentResponseDto,
                commentResponseDto,
                commentResponseDto
        );

        // when then
        when(commentService.getThreeLastCommentsForPostByUsernameAndPostId(any(), any(), pageable))
                .thenReturn(commentResponseDtoList);

        mockMvc.perform(
                        get("/comments/by-user/{username}", commentResponseDto.getUsername())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].username").value(commentResponseDto.getUsername()))
                .andExpect(jsonPath("$[0].text").value(commentResponseDto.getText()));

    }

    @Test
    void should_deleteComment_byId() throws Exception {
        // given
        CommentResponseDto commentResponseDto = initCommentResponseDto();

        // when then
        mockMvc.perform(
                        delete("/comments/{commentId}", commentResponseDto.getPostId())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

}