package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import com.jd.twitterclonebackend.dto.response.RepliedPostResponseDto;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.exception.PostException;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidPostEnum;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.mapper.CommentMapper;
import com.jd.twitterclonebackend.mapper.PostMapper;
import com.jd.twitterclonebackend.repository.CommentRepository;
import com.jd.twitterclonebackend.repository.ImageFileRepository;
import com.jd.twitterclonebackend.repository.PostRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    private final ImageFileRepository imageFileRepository;
    private final CommentRepository commentRepository;

    private final UserDetailsServiceImpl userDetailsService;
    private final FileService fileService;

    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Override
    public void addPost(MultipartFile[] files, String postRequestJson) {
        // Get User who created post
        UserEntity userEntity = userDetailsService.currentLoggedUserEntity();
        // Map Post from request to post entity
        PostEntity postEntity = postMapper.mapFromDtoToEntity(
                postRequestJson,
                userEntity
        );
        // Save mapped post in repository
        postRepository.save(postEntity);
        // If file is not empty - upload into db

        if (Objects.nonNull(files)) {
            Arrays.stream(files)
                    .forEach(file -> fileService.uploadImageFile(postEntity, file));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts(Pageable pageable) {

        List<Long> likedPostIdListByLoggedUser = userDetailsService.currentLoggedUserEntity()
                .getLikedPosts()
                .stream()
                .map(PostEntity::getId)
                .toList();

        return postRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .stream()
                .map(postMapper::mapFromEntityToDto)
                .map(fileService::getAllImageFilesForPost)
                .peek(postResponseDto -> {
                    if (likedPostIdListByLoggedUser.contains(postResponseDto.getId())) {
                        postResponseDto.setLikedByLoggedUser(true);
                    }
                })
                .toList();
    }

    @Override
    public PostResponseDto getPostById(Long postId) {
        // Find post by id or else throw exception
        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostException(
                        InvalidPostEnum.POST_NOT_FOUND_WITH_ID.getMessage() + postId,
                        HttpStatus.NOT_FOUND
                ));
        // Map Post from entity to dto, get image files and return
        PostResponseDto postResponseDto = postMapper.mapFromEntityToDto(postEntity);
        return fileService.getAllImageFilesForPost(postResponseDto);
    }

    @Override
    @Transactional
    public List<PostResponseDto> getPostsByUsername(String username, Pageable pageable) {
        List<Long> likedPostIdListByLoggedUser = userDetailsService.currentLoggedUserEntity()
                .getLikedPosts()
                .stream()
                .map(PostEntity::getId)
                .toList();

        // Find user in user repository
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username,
                        HttpStatus.NOT_FOUND
                ));

        // Find all posts by user entity, map them to dto and return
        return postRepository.findAllByUserAndOrderByCreatedAtDesc(userEntity, pageable)
                .stream()
                .map(postMapper::mapFromEntityToDto)
                .map(fileService::getAllImageFilesForPost)
                .peek(postResponseDto -> {
                    if (likedPostIdListByLoggedUser.contains(postResponseDto.getId())) {
                        postResponseDto.setLikedByLoggedUser(true);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePostById(Long postId) {
        // TODO: check if cascade words - should delete comments and images from repo
        // todo: liked post doesnt delete
        imageFileRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);
//        userRepository.deleteLikedPostsByPostId(postId);
        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public void likePostById(Long postId) {
        UserEntity userEntity = userDetailsService.currentLoggedUserEntity();

        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostException(
                        InvalidPostEnum.POST_NOT_FOUND_WITH_ID.getMessage() + postId,
                        HttpStatus.NOT_FOUND
                ));

        // If user like post then dislike it -> remove from postLies
        boolean doesUserLikeCurrentPost = postEntity.getUserLikes().contains(userEntity);
        if (doesUserLikeCurrentPost) {
            userEntity.getLikedPosts().remove(postEntity);
        } else {
            userEntity.getLikedPosts().add(postEntity);
        }
        userRepository.save(userEntity);

        // todo: send notification to post.getUserId() that logged user likes his post
    }

    @Override
    @Transactional
    public List<PostResponseDto> getLikedPostsByUsername(String username, Pageable pageable) {

        UserEntity loggedUser = userDetailsService.currentLoggedUserEntity();

        if (loggedUser.getUsername().equals(username)) {
            return getLikedPostResponseDtoListForLoggedUser(
                    pageable,
                    loggedUser
            );
        }

        return getLikedPostResponseDtoListForRegularUser(
                username,
                pageable,
                loggedUser
        );
    }

    private List<PostResponseDto> getLikedPostResponseDtoListForLoggedUser(Pageable pageable,
                                                                           UserEntity loggedUser) {

        return postRepository.findByUserLikes(loggedUser, pageable)
                .stream()
                .map(postMapper::mapFromEntityToDto)
                .map(fileService::getAllImageFilesForPost)
                .peek(postResponseDto -> postResponseDto.setLikedByLoggedUser(true))
                .sorted(Comparator.comparing(PostResponseDto::getCreatedAt).reversed())
                .toList();
    }

    private List<PostResponseDto> getLikedPostResponseDtoListForRegularUser(String username,
                                                                            Pageable pageable,
                                                                            UserEntity loggedUser) {
        List<Long> likedPostIdListByLoggedUser = loggedUser
                .getLikedPosts()
                .stream()
                .map(PostEntity::getId)
                .toList();

        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username,
                        HttpStatus.NOT_FOUND
                ));

        // Find all posts liked by user entity, map them to dto and return
        return postRepository.findByUserLikes(userEntity, pageable)
                .stream()
                .map(postMapper::mapFromEntityToDto)
                .map(fileService::getAllImageFilesForPost)
                .peek(postResponseDto -> {
                    if (likedPostIdListByLoggedUser.contains(postResponseDto.getId())) {
                        postResponseDto.setLikedByLoggedUser(true);
                    }
                })
                .sorted(Comparator.comparing(PostResponseDto::getCreatedAt).reversed())
                .toList();
    }

    @Override
    public List<RepliedPostResponseDto> getRepliedPostsWithCommentsByUsername(String username, Pageable pageable) {

        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username,
                        HttpStatus.NOT_FOUND
                ));

        List<Long> likedPostIdListByLoggedUser = userDetailsService.currentLoggedUserEntity()
                .getLikedPosts()
                .stream()
                .map(PostEntity::getId)
                .toList();

        // Limit result to last 3
        Pageable commentRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "createdAt");

        List<PostResponseDto> postResponseDtoList = postRepository.findPostByCommentsFromUsername(userEntity, pageable)
                .stream()
                .map(postMapper::mapFromEntityToDto)
                .peek(postResponseDto -> {
                    if (likedPostIdListByLoggedUser.contains(postResponseDto.getId())) {
                        postResponseDto.setLikedByLoggedUser(true);
                    }
                })
                .toList();

        // Get all comments created by user, map them to dto, collect to list and return
        return postResponseDtoList.stream().map(postResponseDto -> {
            List<CommentResponseDto> commentResponseDtoList = commentRepository.findAllByUserAndOrderByCreatedAtDesc(userEntity,
                            postResponseDto.getId(),
                            commentRequest)
                    .stream()
                    .map(commentMapper::mapFromEntityToDto)
                    .toList();
            return postMapper.mapToRepliedPostResponse(postResponseDto, commentResponseDtoList);
        }).toList();


    }


}
