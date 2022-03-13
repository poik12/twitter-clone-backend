package com.jd.twitterclonebackend.integration.service;

import com.jd.twitterclonebackend.dto.request.PostRequestDto;
import com.jd.twitterclonebackend.entity.ImageFileEntity;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.exception.PostException;
import com.jd.twitterclonebackend.exception.enums.InvalidPostEnum;
import com.jd.twitterclonebackend.integration.IntegrationTestInitData;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class PostServiceImplTest extends IntegrationTestInitData {

    @Test
    void should_addPost_withoutFile() {
        // given
        initCurrentLoggedUser();
        PostRequestDto postRequestDto = initPostRequestDto();
        String postRequestJson = initRequestDtoAsJson(postRequestDto);

        // when
        postService.addPost(null, postRequestJson);

        // then
        assertThat(fileRepository.findAll()).hasSize(0);
        assertThat(postRepository.findAll()).hasSize(1);
        PostEntity postEntity = postRepository.findByDescription(POST_DESCRIPTION);
        assertThat(postEntity.getDescription()).isEqualTo(POST_DESCRIPTION);
    }

    @Test
    void should_addPost_withFile() {
        // given
        initCurrentLoggedUser();
        PostRequestDto postRequestDto = initPostRequestDto();
        String postRequestJson = initRequestDtoAsJson(postRequestDto);
        MultipartFile file = initMultiPartFile();

        // when
        postService.addPost(file, postRequestJson);

        // then
        assertThat(fileRepository.findAll()).hasSize(1);
        assertThat(postRepository.findAll()).hasSize(1);

        PostEntity postEntity = postRepository.findByDescription(POST_DESCRIPTION);
        assertThat(postEntity.getDescription()).isEqualTo(POST_DESCRIPTION);

        ImageFileEntity fileEntity = fileRepository.getByPost(postEntity);
        assertThat(fileEntity.getContent()).isNotNull();
    }

    @Test
    void should_getAllPosts_sortedByTimeStamp() {
        // given
        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.Direction.DESC,
                "createdAt"
        );


        List<PostEntity> postEntityList = initPostsInDatabase();
        assertThat(postEntityList).hasSize(3);

        // when
        var result = postService.getAllPosts(pageable);

        // todo: doesn't work
        // then
//        assertThat(result.size()).isEqualTo(postEntityList.size());
//        PostResponseDto postResponseDto = result.get(0);
//        PostEntity postEntity = postEntityList.get(0);
//        byte[] content = postEntity.getImages().get(0).getContent();
//        assertThat(postResponseDto.getFileContent()).isEqualTo(content);
    }


    @Test
    void should_getPostDto_byPostId() {
        // given
        List<PostEntity> postEntityList = initPostsInDatabase();
        assertThat(postEntityList).hasSize(3);
        postEntityList.forEach(postEntity -> {
            System.out.println(postEntity.getId());
            System.out.println(postEntity.getImages());
        });
        long postId = 2L;


        // TODO: IN POST ENTITY IMAGE LIST IS EMPTY
        // when
        var response = postService.getPostById(postId);

        // then
        PostEntity postEntity = postEntityList.get(0);

        assertThat(response.getId()).isEqualTo(postEntity.getId());
        assertThat(response.getName()).isEqualTo(postEntity.getUser().getName());
        assertThat(response.getUsername()).isEqualTo(postEntity.getUser().getUsername());
        assertThat(response.getDescription()).isEqualTo(postEntity.getDescription());
        assertThat(response.getCreatedAt()).isEqualTo(postEntity.getCreatedAt());
        assertThat(response.getCommentNo()).isEqualTo(postEntity.getCommentNo());
        assertThat(response.getUserProfilePicture()).isEqualTo(postEntity.getUser().getProfilePicture());

        ImageFileEntity fileEntity = postEntity.getImages().get(0);
        assertThat(response.getFileContent()).isEqualTo(fileEntity.getContent());

    }

    @Test
    void should_throwPostException_whenPostWasNotFoundById() {
        // given
        List<PostEntity> postEntityList = initPostsInDatabase();
        assertThat(postEntityList).hasSize(3);
        Long postId = 3L;

        // when
        var result = assertThrows(
                PostException.class,
                () -> postService.getPostById(postId)
        );

        // then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getMessage()).isEqualTo( InvalidPostEnum.POST_NOT_FOUND_WITH_ID.getMessage() + postId);

    }

    @Test
    void getPostsByUsername() {
    }

    @Test
    void deletePostById() {
    }
}