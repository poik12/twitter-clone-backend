package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.domain.CommentEntity;
import com.jd.twitterclonebackend.domain.PostEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.CommentRequest;
import com.jd.twitterclonebackend.dto.CommentResponse;
import com.jd.twitterclonebackend.exception.PostException;
import com.jd.twitterclonebackend.mapper.CommentMapper;
import com.jd.twitterclonebackend.repository.CommentRepository;
import com.jd.twitterclonebackend.repository.PostRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    @Override
    public void createComment(CommentRequest commentRequest) {

        // Find post for comment
        PostEntity postEntity = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new PostException(
                        "Post with id: " + commentRequest.getPostId() + " for comment being created was now found"
                ));

        // Get user who created post
        UserEntity userEntity = userDetailsService.currentLoggedUserEntity();

        // Map Comment from comment request Dto to comment entity
        CommentEntity commentEntity = commentMapper.mapFromDtoToEntity(
                commentRequest,
                postEntity,
                userEntity
        );

        // Save comment in repository
        commentRepository.save(commentEntity);

        // TODO: notification for user who created post
    }

    @Override
    public List<CommentResponse> getAllCommentsForPost(Long postId) {
        // Find post with comments in post repository
        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostException(
                        "Post with id: " + postId + " for comment being created was now found"
                ));

        // Get all comments for found post, map them to DTO, collect to list and return
        return commentRepository
                .findAllByPost(postEntity)
                .stream()
                .map(commentEntity -> commentMapper.mapFromEntityToDto(commentEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getAllCommentsForUser(String username) {
        // Find user who created comments in user repository by username
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with username: " + username + " was not found")
                );

        // Get all comments created by user, map them to dto, collect to list and return
        return commentRepository
                .findAllByUser(userEntity)
                .stream()
                .map(commentEntity -> commentMapper.mapFromEntityToDto(commentEntity))
                .collect(Collectors.toList());
    }
}
