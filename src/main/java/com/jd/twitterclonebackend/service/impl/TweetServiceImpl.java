package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import com.jd.twitterclonebackend.dto.response.RepliedTweetResponseDto;
import com.jd.twitterclonebackend.dto.response.TweetResponseDto;
import com.jd.twitterclonebackend.entity.HashtagEntity;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.entity.enums.NotificationType;
import com.jd.twitterclonebackend.exception.TweetException;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidTweetEnum;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.mapper.CommentMapper;
import com.jd.twitterclonebackend.mapper.TweetMapper;
import com.jd.twitterclonebackend.repository.*;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.service.NotificationService;
import com.jd.twitterclonebackend.service.TweetService;
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
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final ImageFileRepository imageFileRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    private final UserDetailsServiceImpl userDetailsService;
    private final FileService fileService;
    private final NotificationService notificationService;


    private final TweetMapper tweetMapper;
    private final CommentMapper commentMapper;

    @Override
    public void addTweet(MultipartFile[] files, String tweetRequestJson) {
        // Get User who created post
        UserEntity loggedUser = userDetailsService.currentLoggedUserEntity();
        // Map Post from request to post entity
        TweetEntity tweetEntity = tweetMapper.mapFromDtoToEntity(
                tweetRequestJson,
                loggedUser
        );
        // Save mapped post in repository
        tweetRepository.save(tweetEntity);
        // If file is not empty - upload into db
        if (Objects.nonNull(files)) {
            Arrays.stream(files)
                    .forEach(file -> fileService.uploadImageFile(tweetEntity, file));
        }
        // Send notification to user followers
        loggedUser.getFollowers().forEach(followerEntity -> notificationService.notifyUser(
                followerEntity.getFrom(),
                NotificationType.TWEET,
                tweetEntity.getId()
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TweetResponseDto> getAllTweets(Pageable pageable) {

        List<Long> likedPostIdListByLoggedUser = userDetailsService.currentLoggedUserEntity()
                .getLikedTweets()
                .stream()
                .map(TweetEntity::getId)
                .toList();

        return tweetRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .stream()
                .map(tweetMapper::mapFromEntityToDto)
                .map(fileService::getAllImageFilesForPost)
                .peek(postResponseDto -> {
                    if (likedPostIdListByLoggedUser.contains(postResponseDto.getId())) {
                        postResponseDto.setLikedByLoggedUser(true);
                    }
                })
                .toList();
    }

    @Override
    public TweetResponseDto getTweetById(Long tweetId) {
        // Find post by id or else throw exception
        TweetEntity tweetEntity = tweetRepository
                .findById(tweetId)
                .orElseThrow(() -> new TweetException(
                        InvalidTweetEnum.TWEET_NOT_FOUND_WITH_ID.getMessage() + tweetId,
                        HttpStatus.NOT_FOUND
                ));
        // Map Post from entity to dto, get image files and return
        TweetResponseDto tweetResponseDto = tweetMapper.mapFromEntityToDto(tweetEntity);
        return fileService.getAllImageFilesForPost(tweetResponseDto);
    }

    @Override
    @Transactional
    public List<TweetResponseDto> getTweetsByUsername(String username, Pageable pageable) {
        List<Long> likedPostIdListByLoggedUser = userDetailsService.currentLoggedUserEntity()
                .getLikedTweets()
                .stream()
                .map(TweetEntity::getId)
                .toList();

        // Find user in user repository
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username,
                        HttpStatus.NOT_FOUND
                ));

        // Find all posts by user entity, map them to dto and return
        return tweetRepository.findAllByUserAndOrderByCreatedAtDesc(userEntity, pageable)
                .stream()
                .map(tweetMapper::mapFromEntityToDto)
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
    public void deleteTweetById(Long tweetId) {
        tweetRepository
                .findById(tweetId)
                .ifPresentOrElse(
                        TweetEntity::removeLikedTweetFromUsers,
                        () -> {
                            throw new TweetException(
                                    InvalidTweetEnum.TWEET_NOT_FOUND_WITH_ID.getMessage() + tweetId,
                                    HttpStatus.NOT_FOUND
                            );
                        }
                );
        notificationRepository.deleteByMaterialId(tweetId);
        imageFileRepository.deleteByTweetId(tweetId);
        commentRepository.deleteByTweetId(tweetId);
        tweetRepository.deleteById(tweetId);
    }

    @Override
    @Transactional
    public void likeTweetById(Long tweetId) {
        UserEntity loggedUser = userDetailsService.currentLoggedUserEntity();

        TweetEntity tweetEntity = tweetRepository
                .findById(tweetId)
                .orElseThrow(() -> new TweetException(
                        InvalidTweetEnum.TWEET_NOT_FOUND_WITH_ID.getMessage() + tweetId,
                        HttpStatus.NOT_FOUND
                ));

        // If user like post then dislike it -> remove from postLies
        boolean doesUserLikeCurrentPost = tweetEntity.getUserLikes().contains(loggedUser);
        if (doesUserLikeCurrentPost) {
            loggedUser.getLikedTweets().remove(tweetEntity);
        } else {
            loggedUser.getLikedTweets().add(tweetEntity);
            // Send notification to tweet publisher
            notificationService.notifyUser(
                    tweetEntity.getUser(),
                    NotificationType.LIKE,
                    tweetEntity.getId()
            );
        }
        userRepository.save(loggedUser);
    }

    @Override
    @Transactional
    public List<TweetResponseDto> getLikedTweetsByUsername(String username, Pageable pageable) {

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

    private List<TweetResponseDto> getLikedPostResponseDtoListForLoggedUser(Pageable pageable,
                                                                            UserEntity loggedUser) {

        return tweetRepository.findByUserLikes(loggedUser, pageable)
                .stream()
                .map(tweetMapper::mapFromEntityToDto)
                .map(fileService::getAllImageFilesForPost)
                .peek(postResponseDto -> postResponseDto.setLikedByLoggedUser(true))
                .sorted(Comparator.comparing(TweetResponseDto::getCreatedAt).reversed())
                .toList();
    }

    private List<TweetResponseDto> getLikedPostResponseDtoListForRegularUser(String username,
                                                                             Pageable pageable,
                                                                             UserEntity loggedUser) {
        List<Long> likedPostIdListByLoggedUser = loggedUser
                .getLikedTweets()
                .stream()
                .map(TweetEntity::getId)
                .toList();

        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username,
                        HttpStatus.NOT_FOUND
                ));

        // Find all posts liked by user entity, map them to dto and return
        return tweetRepository.findByUserLikes(userEntity, pageable)
                .stream()
                .map(tweetMapper::mapFromEntityToDto)
                .map(fileService::getAllImageFilesForPost)
                .peek(postResponseDto -> {
                    if (likedPostIdListByLoggedUser.contains(postResponseDto.getId())) {
                        postResponseDto.setLikedByLoggedUser(true);
                    }
                })
                .sorted(Comparator.comparing(TweetResponseDto::getCreatedAt).reversed())
                .toList();
    }

    @Override
    public List<RepliedTweetResponseDto> getRepliedTweetsWithCommentsByUsername(String username, Pageable pageable) {

        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username,
                        HttpStatus.NOT_FOUND
                ));

        List<Long> likedPostIdListByLoggedUser = userDetailsService.currentLoggedUserEntity()
                .getLikedTweets()
                .stream()
                .map(TweetEntity::getId)
                .toList();

        // Limit result to last 3
        Pageable commentRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "createdAt");

        List<TweetResponseDto> tweetResponseDtoList = tweetRepository.findPostByCommentsFromUsername(userEntity, pageable)
                .stream()
                .map(tweetMapper::mapFromEntityToDto)
                .peek(postResponseDto -> {
                    if (likedPostIdListByLoggedUser.contains(postResponseDto.getId())) {
                        postResponseDto.setLikedByLoggedUser(true);
                    }
                })
                .toList();

        // Get all comments created by user, map them to dto, collect to list and return
        return tweetResponseDtoList.stream()
                .map(postResponseDto -> {
                            List<CommentResponseDto> commentResponseDtoList = commentRepository
                                    .findAllByUserAndOrderByCreatedAtDesc(
                                            userEntity,
                                            postResponseDto.getId(),
                                            commentRequest
                                    )
                                    .stream()
                                    .map(commentMapper::mapFromEntityToDto)
                                    .toList();
                            return tweetMapper.mapToRepliedTweetResponse(
                                    postResponseDto,
                                    commentResponseDtoList
                            );
                        }
                ).toList();
    }

    @Override
    public List<TweetResponseDto> searchTweets(String searchTerm, Pageable pageable) {
        // todo: searching doesn't work
        String searchTermWithHashtag = "#" + searchTerm;
        return tweetRepository.findByHashTag(searchTermWithHashtag)
                .stream()
                .map(tweetMapper::mapFromEntityToDto)
                .collect(Collectors.toList());

    }


}
