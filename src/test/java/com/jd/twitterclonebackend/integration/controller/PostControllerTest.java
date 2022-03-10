package com.jd.twitterclonebackend.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.twitterclonebackend.controller.AuthController;
import com.jd.twitterclonebackend.controller.PostController;
import com.jd.twitterclonebackend.controller.handler.AuthControllerExceptionHandler;
import com.jd.twitterclonebackend.controller.handler.PostControllerExceptionHandler;
import com.jd.twitterclonebackend.dto.request.PostRequestDto;
import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import com.jd.twitterclonebackend.service.AuthService;
import com.jd.twitterclonebackend.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class PostControllerTest extends IntegrationTestInitData {

    public MockMvc mockMvc;

    @Autowired
    public PostController postController;

    @MockBean
    public PostService postService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new PostControllerExceptionHandler())
                .build();
    }

    @Test
    void should_addPostRequestWithFile() throws Exception {
        // given
        PostRequestDto postRequestDto = initPostRequestDto();
        String postRequestJson = initRequestDtoAsJson(postRequestDto);
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
        PostResponseDto postResponseDto = initPostResponseDto();

        List<PostResponseDto> postResponseDtoList = List.of(
                postResponseDto,
                postResponseDto,
                postResponseDto
        );

        // when then
        when(postService.getAllPosts()).thenReturn(postResponseDtoList);

        mockMvc.perform(
                        get("/posts")
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(postResponseDto.getId()))
                .andExpect(jsonPath("$[0].name").value(postResponseDto.getName()));
    }

    @Test
    void should_getPostResponseDto_byPostId() throws Exception {
        // given
        PostResponseDto postResponseDto = initPostResponseDto();

        // when then
        when(postService.getPostById(any())).thenReturn(postResponseDto);

        mockMvc.perform(
                        get("/posts/{postId}", postResponseDto.getId())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postResponseDto.getId()))
                .andExpect(jsonPath("$.name").value(postResponseDto.getName()));

    }

    @Test
    void should_getPostResponseDtoList_byUsername() throws Exception {
        // given
        PostResponseDto postResponseDto = initPostResponseDto();

        List<PostResponseDto> postResponseDtoList = List.of(
                postResponseDto,
                postResponseDto,
                postResponseDto
        );

        // when then
        when(postService.getPostsByUsername(any())).thenReturn(postResponseDtoList);

        mockMvc.perform(
                        get("/posts/by-user/{username}", postResponseDto.getUsername())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(postResponseDto.getId()))
                .andExpect(jsonPath("$[0].name").value(postResponseDto.getName()));
    }

    @Test
    void should_deletePost_byId() throws Exception {
        // given
        PostResponseDto postResponseDto = initPostResponseDto();

        // when then
        mockMvc.perform(
                        delete("/posts/{postId}", postResponseDto.getId())
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}