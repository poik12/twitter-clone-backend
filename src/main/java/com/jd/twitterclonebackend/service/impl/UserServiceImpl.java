package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.domain.FollowerEntity;
import com.jd.twitterclonebackend.domain.UserEntity;
import com.jd.twitterclonebackend.dto.UserRequest;
import com.jd.twitterclonebackend.dto.UserResponse;
import com.jd.twitterclonebackend.mapper.UserMapper;
import com.jd.twitterclonebackend.repository.FollowerRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserDetailsServiceImpl userDetailsService;
    private final FollowerRepository followerRepository;

    @Override
    public UserResponse getUserByUsername(String username) {
        // Find user in repository by its username
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        // Map user entity to user response
        return userMapper.mapFromEntityToDto(userEntity);
    }

    @Override
    public List<UserEntity> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public void updateUserByUsername(String username,
                                     UserRequest userRequest,
                                     MultipartFile profileImageFile,
                                     MultipartFile backgroundImageFile) {
        // Find user in repository by its username
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // Map user response to user entity
        UserEntity updatedUserEntity = userMapper.mapFromDtoToEntity(
                userEntity,
                userRequest,
                profileImageFile,
                backgroundImageFile
        );
        // Save updated user entity in db
        userRepository.save(updatedUserEntity);
    }

    @Override
    @Transactional
    public void followUser(String username) {
        // Find logged user
        UserEntity loggedUserEntity = userDetailsService.currentLoggedUserEntity();
        // Find user who will be followed by its username
        UserEntity userToFollow = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with username: " + username + " was not found")
                );
        // Create object of follower entity and save it in db
        FollowerEntity followerEntity = new FollowerEntity(
                userToFollow,
                loggedUserEntity
        );
        followerRepository.save(followerEntity);
    }

    @Override
    public List<UserResponse> getAllFollowers() {

        UserEntity loggedUserEntity = userDetailsService.currentLoggedUserEntity();

        return followerRepository
                .getAllFollowersByUser(loggedUserEntity)
                .stream()
                .map(userEntity -> userMapper.mapFromEntityToDto(userEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllFollowings() {

        UserEntity loggedUserEntity = userDetailsService.currentLoggedUserEntity();

        return followerRepository
                .getAllFollowingsByUser(loggedUserEntity)
                .stream()
                .map(userEntity -> userMapper.mapFromEntityToDto(userEntity))
                .collect(Collectors.toList());
    }
}
