package com.jd.twitterclonebackend.integration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.PostRequestDto;
import com.jd.twitterclonebackend.integration.InitIntegrationTestData;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

class PostServiceImplTest extends InitIntegrationTestData {

    @Test
    void should_addPost_withoutFile() {
        // given
        UserEntity userEntity = initCurrentLoggedUser();

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .description(POST_DESCRIPTION)
                .build();

        String postRequestJSON = null;
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            postRequestJSON = objectWriter.writeValueAsString(postRequestDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // when
        postService.addPost(null, postRequestJSON);

        // then
        assertThat(fileRepository.findAll()).hasSize(0);
        assertThat(postRepository.findAll()).hasSize(1);
        PostEntity postEntity = postRepository.findByDescription(POST_DESCRIPTION);
        assertThat(postEntity.getDescription()).isEqualTo(POST_DESCRIPTION);
    }
    
    @Test
    void addPost() {
    }

    @Test
    void getAllPosts() {
    }

    @Test
    void getPostById() {
    }

    @Test
    void getPostsByUsername() {
    }

    @Test
    void deletePostById() {
    }
}