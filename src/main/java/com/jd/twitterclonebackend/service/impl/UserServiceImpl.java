package com.jd.twitterclonebackend.service.impl;

import com.jd.twitterclonebackend.dto.FollowerDto;
import com.jd.twitterclonebackend.entity.FollowerEntity;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.dto.UserRequestDto;
import com.jd.twitterclonebackend.dto.UserResponseDto;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.mapper.UserMapper;
import com.jd.twitterclonebackend.mapper.mapstruct.NewUserMapper;
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
    public UserResponseDto getUserByUsername(String username) {
        // Find user in repository by its username
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        // Map user entity to user response
        return userMapper.mapFromEntityToUserDto(userEntity);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        log.info("Fetching all users");
        return userRepository
                .findAll()
                .stream()
                .map(userEntity -> userMapper.mapFromEntityToUserDto(userEntity))
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserByUsername(String username,
                                     UserRequestDto userRequestDto,
                                     MultipartFile profileImageFile,
                                     MultipartFile backgroundImageFile) {
        // Find user in repository by its username
        UserEntity userEntity = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        // Map user response to user entity
        UserEntity updatedUserEntity = userMapper.mapFromUserDtoToEntity(
                userEntity,
                userRequestDto,
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
                        new UserException(InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME + username)
                );
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
    }

    @Transactional
    @Override
    public void unfollowUser(String username) {
        // Find logged user
        UserEntity loggedUserEntity = userDetailsService.currentLoggedUserEntity();
        // Find user who will be unfollowed by its username
        UserEntity userToUnfollow = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserException(InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME + username)
                );
        // Find follower entity in db and delete
        FollowerEntity followerEntity = followerRepository.findByToAndFrom(
                userToUnfollow,
                loggedUserEntity
        ).orElseThrow(() -> {
            throw new RuntimeException("Follower entity does not exist");
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

        UserResponseDto followerUserEntity = getUserByUsername(followingUser);

        return followerUserEntity.getFollowing()
                .stream()
                .map(FollowerDto::getUsername)
                .anyMatch(followingUsername -> followingUsername.equals(followedUser));
    }
}
