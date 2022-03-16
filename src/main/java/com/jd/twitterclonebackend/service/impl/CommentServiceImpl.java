package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.dto.request.CommentRequestDto;
import com.jd.twitterclonebackend.dto.response.CommentResponseDto;
import com.jd.twitterclonebackend.entity.CommentEntity;
import com.jd.twitterclonebackend.entity.TweetEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.exception.CommentException;
import com.jd.twitterclonebackend.exception.TweetException;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidCommentEnum;
import com.jd.twitterclonebackend.exception.enums.InvalidTweetEnum;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.mapper.CommentMapper;
import com.jd.twitterclonebackend.repository.CommentRepository;
import com.jd.twitterclonebackend.repository.TweetRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    private final UserDetailsServiceImpl userDetailsService;

    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public void addComment(CommentRequestDto commentRequestDto) {
        // Get user who created post
        UserEntity userEntity = userDetailsService.currentLoggedUserEntity();
        // Find post for comment
        TweetEntity tweetEntity = tweetRepository
                .findById(commentRequestDto.getTweetId())
                .orElseThrow(() -> new TweetException(
                        InvalidTweetEnum.TWEET_FOR_COMMENT_NOT_FOUND.getMessage() + commentRequestDto.getTweetId(),
                        HttpStatus.NOT_FOUND
                ));
        // Map Comment from comment request Dto to comment entity
        CommentEntity commentEntity = commentMapper.mapFromDtoToEntity(
                commentRequestDto,
                tweetEntity,
                userEntity
        );
        // Save comment in repository
        commentRepository.save(commentEntity);
        // Increment no of comments in post
        tweetEntity.setCommentNo(tweetEntity.getCommentNo() + 1);
        tweetRepository.save(tweetEntity);

        // TODO: notification for user who created post
    }

    @Override
    public List<CommentResponseDto> getAllCommentsForPost(Long tweetId, Pageable pageable) {
        // Find post with comments in post repository
        TweetEntity tweetEntity = tweetRepository
                .findById(tweetId)
                .orElseThrow(() -> new TweetException(
                        InvalidTweetEnum.TWEET_FOR_COMMENT_NOT_FOUND.getMessage() + tweetId,
                        HttpStatus.NOT_FOUND
                ));
        // Get all comments for found post, map them to DTO and collect to list
        return commentRepository
                .findAllByPostAndOrderByCreatedAtDesc(tweetEntity, pageable)
                .stream()
                .map(commentMapper::mapFromEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDto> getThreeLastCommentsForPostByUsernameAndPostId(String username,
                                                                                   Long tweetId,
                                                                                   Pageable pageable) {
        // Find user who created comments in user repository by username
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username,
                        HttpStatus.NOT_FOUND
                ));

        // Get all comments created by user, map them to dto, collect to list and return
        return commentRepository
                .findAllByUserAndOrderByCreatedAtDesc(userEntity, tweetId, pageable)
                .stream()
                .map(commentMapper::mapFromEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCommentById(Long commentId) {
        CommentEntity commentEntity = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new CommentException(
                        InvalidCommentEnum.COMMENT_NOT_FOUND_WITH_ID.getMessage() + commentId,
                        HttpStatus.NOT_FOUND
                ));
        commentRepository.delete(commentEntity);
    }
}
