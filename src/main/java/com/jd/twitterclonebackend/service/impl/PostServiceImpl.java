package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.domain.PostEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.PostRequestDto;
import com.jd.twitterclonebackend.dto.PostResponseDto;
import com.jd.twitterclonebackend.enums.InvalidPostEnum;
import com.jd.twitterclonebackend.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.exception.PostException;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.mapper.PostMapper;
import com.jd.twitterclonebackend.repository.CommentRepository;
import com.jd.twitterclonebackend.repository.ImageFileRepository;
import com.jd.twitterclonebackend.repository.PostRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final PostMapper postMapper;
    private final FileService fileService;
    private final ImageFileRepository imageFileRepository;

    @Override
    public void addPost(MultipartFile file, PostRequestDto postRequestDto) {
        // Get User who created post
        UserEntity userEntity = userDetailsService.currentLoggedUserEntity();
        // Map Post from request to post entity
        PostEntity postEntity = postMapper.mapFromDtoToEntity(postRequestDto, userEntity);
        // Save mapped post in repository
        postRepository.save(postEntity);
        // If file is not empty - upload into db
        if (Objects.nonNull(file)) {
            fileService.uploadImageFile(
                    postEntity,
                    file
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        // Get map of post id and content in byte from db
        Map<Long, byte[]> imageFilesMap = fileService.getAllImageFiles();

        //Find all post in repository and map them from entities to dto
        return postRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(postEntity -> postMapper.mapFromEntityToDto(
                        postEntity,
                        imageFilesMap
                ))
                .collect(Collectors.toList());
    }

    @Override
    public PostResponseDto getPostById(Long postId) {
        // Find post by id
        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(() ->
                        new PostException(InvalidPostEnum.POST_NOT_FOUND.getMessage() + postId)
                );

        // Collect found post entity to list
        List<PostEntity> postList = new ArrayList<>();
        postList.add(postEntity);

        // Find image file in db by post entity
        Map<Long, byte[]> imageFile = fileService.getImageFilesByPostList(postList);
        // Map Post from entity to dto and return
        return postMapper.mapFromEntityToDto(
                postEntity,
                imageFile
        );
    }

    // TODO: update map
    @Override
    @Transactional
    public List<PostResponseDto> getPostsByUsername(String username) {
        // Find user in user repository
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserException(InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username)
                );

        // Find all posts by user entity, map them to dto and return
        List<PostEntity> postList = postRepository.findByUser(userEntity);

        Map<Long, byte[]> imageFileList = fileService.getImageFilesByPostList(postList);

        // Find all posts by user entity, map them to dto and return
        return postRepository
                .findByUser(userEntity)
                .stream()
                .map(postEntity -> postMapper.mapFromEntityToDto(
                        postEntity,
                        imageFileList
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePostById(Long postId) {
        imageFileRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);
        postRepository.deleteById(postId);
    }
}
