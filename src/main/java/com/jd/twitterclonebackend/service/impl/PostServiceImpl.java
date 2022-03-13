package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.dto.response.PostResponseDto;
import com.jd.twitterclonebackend.entity.PostEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.exception.PostException;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidPostEnum;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.mapper.PostMapper;
import com.jd.twitterclonebackend.repository.PostRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final UserDetailsServiceImpl userDetailsService;
    private final FileService fileService;

    private final PostMapper postMapper;

    @Override
    public void addPost(MultipartFile file, String postRequestJson) {
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
        if (Objects.nonNull(file)) {
            fileService.uploadImageFile(
                    postEntity,
                    file
            );
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

        // Get map of post id and content in byte from db
        Map<Long, byte[]> imageFilesMap = fileService.getAllImageFiles();

        //Find all posts in repository sorted by created timestamp and map them from entities to dto
        return postRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .stream()
                .map(postEntity -> postMapper.mapFromEntityToDto(
                        postEntity,
                        imageFilesMap
                ))
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
        // Create list of that post entity to use file service method
        List<PostEntity> postList = List.of(postEntity);
        // Find image file in db by post entity
        Map<Long, byte[]> imageFile = fileService.getImageFilesByPostList(postList);
        // Map Post from entity to dto and return
        return postMapper.mapFromEntityToDto(
                postEntity,
                imageFile
        );
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
        List<PostEntity> postList = postRepository.findAllByUserAndOrderByCreatedAtDesc(userEntity, pageable);
        // Get image map -> key: postId, value: imageEntity
        Map<Long, byte[]> imageFileMap = fileService.getImageFilesByPostList(postList);
        // Map post entity to dto and collect to list
        return postList.stream()
                .map(postEntity -> postMapper.mapFromEntityToDto(
                        postEntity,
                        imageFileMap
                ))
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
//        imageFileRepository.deleteByPostId(postId);
//        commentRepository.deleteByPostId(postId);
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
        List<PostEntity> postEntityListLikedByLoggedUser = postRepository.findByUserLikes(
                loggedUser,
                pageable
        );
        // Get image map -> key: postId, value: imageEntity
        Map<Long, byte[]> imageFileMap = fileService.getImageFilesByPostList(postEntityListLikedByLoggedUser);

        return postEntityListLikedByLoggedUser
                .stream()
                .map(postEntity -> postMapper.mapFromEntityToDto(
                        postEntity,
                        imageFileMap
                ))
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

        List<PostEntity> postEntityListLikedByUser = postRepository.findByUserLikes(
                userEntity,
                pageable
        );

        // Get image map -> key: postId, value: imageEntity
        Map<Long, byte[]> imageFileMap = fileService.getImageFilesByPostList(postEntityListLikedByUser);

        // Find all posts liked by user entity, map them to dto and return
        return postEntityListLikedByUser
                .stream()
                .map(postEntity -> postMapper.mapFromEntityToDto(
                        postEntity,
                        imageFileMap
                ))
                .peek(postResponseDto -> {
                    if (likedPostIdListByLoggedUser.contains(postResponseDto.getId())) {
                        postResponseDto.setLikedByLoggedUser(true);
                    }
                })
                .sorted(Comparator.comparing(PostResponseDto::getCreatedAt).reversed())
                .toList();
    }

}
