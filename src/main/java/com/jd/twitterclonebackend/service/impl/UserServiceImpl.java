package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.dto.response.FollowerResponseDto;
import com.jd.twitterclonebackend.entity.FollowerEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.response.UserResponseDto;
import com.jd.twitterclonebackend.entity.enums.NotificationType;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.mapper.UserMapper;
import com.jd.twitterclonebackend.repository.FollowerRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.service.NotificationService;
import com.jd.twitterclonebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final FollowerRepository followerRepository;

    private final UserDetailsServiceImpl userDetailsService;
    private final NotificationService notificationService;

    private final UserMapper userMapper;

    @Override
    public UserResponseDto getUserByUsername(String username) {
        // Find user in repository by its username
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username,
                        HttpStatus.NOT_FOUND
                ));
        // Map user entity to user response
        return userMapper.mapFromEntityToDto(userEntity);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        log.info("Fetching all users");
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::mapFromEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserDetails(String userDetailsRequestJson,
                                  MultipartFile profileImageFile,
                                  MultipartFile backgroundImageFile) {
        // Get user who updates details
        UserEntity userEntity = userDetailsService.currentLoggedUserEntity();
        // Map user request to user entity
        UserEntity updatedUserEntity = userMapper.mapFromDtoToEntity(
                userEntity,
                userDetailsRequestJson,
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
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME + username,
                        HttpStatus.NOT_FOUND
                ));
        // Create object of follower entity and save it in db
        FollowerEntity followerEntity = new FollowerEntity(
                userToFollow,
                loggedUserEntity
        );
        followerRepository.save(followerEntity);
        // Update no of following in logged user entity
        loggedUserEntity.setFollowingNo(loggedUserEntity.getFollowingNo() + 1);
        userRepository.save(loggedUserEntity);
        // Update no of followers in followed user entity
        userToFollow.setFollowerNo(userToFollow.getFollowerNo() + 1);
        userRepository.save(userToFollow);
        // Send notification to followed user
        notificationService.notifyUser(
                userToFollow,
                NotificationType.FOLLOWER,
                loggedUserEntity.getId()
        );
    }

    @Override
    @Transactional
    public void unfollowUser(String username) {
        // Find logged user
        UserEntity loggedUserEntity = userDetailsService.currentLoggedUserEntity();
        // Find user who will be unfollowed by its username
        UserEntity userToUnfollow = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserException(
                        InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + username,
                        HttpStatus.NOT_FOUND
                ));
        // Find follower entity in db and delete
        FollowerEntity followerEntity = followerRepository.findByToAndFrom(
                userToUnfollow,
                loggedUserEntity
        ).orElseThrow(() -> {
            throw new UserException(
                    InvalidUserEnum.FOLLOWER_ENTITY_DOES_NOT_EXIST.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        });
        followerRepository.delete(followerEntity);
        // Update no of following in logged user entity
        loggedUserEntity.setFollowingNo(loggedUserEntity.getFollowingNo() - 1);
        userRepository.save(loggedUserEntity);
        // Update no of followers in followed user entity
        userToUnfollow.setFollowerNo(userToUnfollow.getFollowerNo() - 1);
        userRepository.save(userToUnfollow);
    }

    @Override
    public boolean checkIfUserIsFollowed(String followingUser, String followedUser) {
        return getUserByUsername(followingUser)
                .getFollowing()
                .stream()
                .map(FollowerResponseDto::getUsername)
                .anyMatch(followingUsername -> followingUsername.equals(followedUser));
    }
}
