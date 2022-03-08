package com.jd.twitterclonebackend.integration.service;

import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.PostRequestDto;
import com.jd.twitterclonebackend.integration.InitIntegrationTestData;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostServiceImplTest extends InitIntegrationTestData {

    @Test
    void should_addPost_whenPostRequestDto() {
        // given
        UserEntity userEntity = initDatabaseByPrimeUserEnabled();
        PostRequestDto postRequestDto = initPostRequestDto();

        // when
        PostEntity postEntity = postMapper.mapFromDtoToEntity(postRequestDto, userEntity);
        postRepository.save(postEntity);

        // then
        assertThat(postEntity.getId()).isEqualTo(1);
        assertThat(postEntity.getDescription()).isEqualTo(postRequestDto.getDescription());
        assertThat(postEntity.getUser()).isEqualTo(userEntity);
        assertThat(postEntity.getCommentNo()).isEqualTo(0);
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