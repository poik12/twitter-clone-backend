package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.domain.CommentEntity;
import com.jd.twitterclonebackend.domain.PostEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.CommentRequestDto;
import com.jd.twitterclonebackend.dto.CommentResponseDto;
import com.jd.twitterclonebackend.enums.InvalidCommentEnum;
import com.jd.twitterclonebackend.enums.InvalidPostEnum;
import com.jd.twitterclonebackend.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.exception.CommentException;
import com.jd.twitterclonebackend.exception.PostException;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.mapper.CommentMapper;
import com.jd.twitterclonebackend.repository.CommentRepository;
import com.jd.twitterclonebackend.repository.PostRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Transactional
    @Override
    public void createComment(CommentRequestDto commentRequestDto) {
        // Get user who created post
        UserEntity userEntity = userDetailsService.currentLoggedUserEntity();
        // Find post for comment
        PostEntity postEntity = postRepository
                .findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new PostException(
                        InvalidPostEnum.POST_FOR_COMMENT_NOT_FOUND.getMessage() + commentRequestDto.getPostId()
                ));
        // Map Comment from comment request Dto to comment entity
        CommentEntity commentEntity = commentMapper.mapFromDtoToEntity(
                commentRequestDto,
                postEntity,
                userEntity
        );
        // Save comment in repository
        commentRepository.save(commentEntity);
        // Increment no of comments in post
        postEntity.setCommentNo(postEntity.getCommentNo() + 1);
        postRepository.save(postEntity);

        // TODO: notification for user who created post
    }

    @Override
    public List<CommentResponseDto> getAllCommentsForPost(Long postId) {
        // Find post with comments in post repository
        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostException(
                        InvalidPostEnum.POST_FOR_COMMENT_NOT_FOUND.getMessage() + postId
                ));
        // Get all comments for found post, map them to DTO and collect to list
        return commentRepository
                .findAllByPost(postEntity)
                .stream()
                .map(commentEntity -> commentMapper.mapFromEntityToDto(commentEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDto> getAllCommentsForUser(String username) {
        // Find user who created comments in user repository by username
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserException(InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username)
                );
        // Get all comments created by user, map them to dto, collect to list and return
        return commentRepository
                .findAllByUser(userEntity)
                .stream()
                .map(commentEntity -> commentMapper.mapFromEntityToDto(commentEntity))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCommentById(Long commentId) {
        CommentEntity commentEntity = commentRepository
                .findById(commentId)
                .orElseThrow(() ->
                        new CommentException(InvalidCommentEnum.COMMENT_NOT_FOUND_WITH_ID.getMessage() + commentId)
                );
        commentRepository.delete(commentEntity);
    }
}
