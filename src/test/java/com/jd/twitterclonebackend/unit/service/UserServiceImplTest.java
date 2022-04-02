package com.jd.twitterclonebackend.unit.service;

import com.jd.twitterclonebackend.dto.request.UserDetailsRequestDto;
import com.jd.twitterclonebackend.dto.response.UserResponseDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.exception.UserException;
import com.jd.twitterclonebackend.exception.enums.InvalidUserEnum;
import com.jd.twitterclonebackend.mapper.UserMapper;
import com.jd.twitterclonebackend.repository.FollowerRepository;
import com.jd.twitterclonebackend.repository.UserRepository;
import com.jd.twitterclonebackend.service.NotificationService;
import com.jd.twitterclonebackend.service.UserService;
import com.jd.twitterclonebackend.service.impl.UserDetailsServiceImpl;
import com.jd.twitterclonebackend.service.impl.UserServiceImpl;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class UserServiceImplTest extends UnitTestInitData {

    private UserService underTest;

    @Mock
    private UserRepository userRepository;
    @Mock
    private FollowerRepository followerRepository;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(
                userRepository,
                followerRepository,
                userDetailsService,
                notificationService,
                userMapper
        );
    }

    @Test
    @DisplayName(value = "Should get UserResponseDto by Username")
    void should_getUserResponseDto_byUsername() {
        // given
        UserEntity userEntity = initUserEntity();
        UserResponseDto userResponseDto = initUserResponseDto(userEntity);
        String username = userEntity.getUsername();

        // when
        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(userEntity));
        when(userMapper.mapFromEntityToDto(any()))
                .thenReturn(userResponseDto);
        var result = underTest.getUserByUsername(username);

        // then
        assertAll(
                () -> {
                    verify(userRepository).findByUsername(any());
                    verify(userMapper).mapFromEntityToDto(any());
                    assertSame(userResponseDto, result);
                }
        );
    }

    @Test
    @DisplayName(value = "Should throw UserException by False Username")
    void should_throwUserException_byFalseUsername() {
        // given
        UserEntity userEntity = initUserEntity();
        UserResponseDto userResponseDto = initUserResponseDto(userEntity);
        String falseUsername = userEntity.getUsername() + "False";

        // when
        var result = assertThrows(
                UserException.class,
                () -> underTest.getUserByUsername(falseUsername)
        );

        // then
        assertAll(
                () -> {
                    assertThatThrownBy(() -> underTest.getUserByUsername(falseUsername))
                            .isInstanceOf(UserException.class)
                            .hasMessageContaining(InvalidUserEnum.USER_NOT_FOUND_WITH_USERNAME.getMessage() + falseUsername);
                    verify(userMapper, never()).mapFromEntityToDto(any());
                }
        );
    }

    @Test
    @DisplayName(value = "Should get List<UserResponseDto>")
    void should_getUserResponseDtoList() {
        // given

        // when
        underTest.getUsers();

        // then
        assertAll(
                () -> {
                    verify(userRepository).findAll();
                }
        );

    }

    @Test
    @DisplayName(value = "Should update UserEntity by UserDetailsRequestJson and ImageFiles")
    void should_updateUserEntity_byUserDetailsRequestJsonWithFiles() {
        // given
        UserEntity userEntity = initUserEntity();
        UserDetailsRequestDto userDetailsRequestDto = initUserRequestDto();
        String userDetailsRequestJson = initRequestDtoAsJson(userDetailsRequestDto);

        // when
        when(userMapper.mapFromDtoToEntity(
                any(), any(),
                any(), any())).thenReturn(userEntity);
        underTest.updateUserDetails(userDetailsRequestJson, null, null);

        // then
        assertAll(
                () -> {
                    verify(userDetailsService).currentLoggedUserEntity();
                    verify(userMapper).mapFromDtoToEntity(
                            any(),
                            any(),
                            any(),
                            any()
                    );
                    ArgumentCaptor<UserEntity> userEntityArgumentCaptor =
                            ArgumentCaptor.forClass(UserEntity.class);
                    verify(userRepository).save(userEntityArgumentCaptor.capture());
                    UserEntity capturedUserEntity = userEntityArgumentCaptor.getValue();
                    assertThat(capturedUserEntity).isEqualTo(userEntity);
                }
        );
    }

    @Test
    @DisplayName(value = "Should follow UserEntity by Username")
    @Disabled
    void should_followUserEntity_byUsername() {
        // given


        // when

        // then

    }

    @Test
    @Disabled
    @DisplayName(value = "Should unfollow UserEntity by Username")
    void should_unfollowUserEntity_byUsername() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName(value = "Should check if UserEntity is followed by FollowingUser and FollowerUser")
    @Disabled
    void should_checkIfUserIdFollowed_byFollowerEntityAndFollowingEntity() {
        // given

        // when

        // then

    }

}