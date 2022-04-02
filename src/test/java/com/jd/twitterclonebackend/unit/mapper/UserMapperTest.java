package com.jd.twitterclonebackend.unit.mapper;

import com.jd.twitterclonebackend.dto.request.UserDetailsRequestDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.FollowerMapper;
import com.jd.twitterclonebackend.mapper.JsonMapper;
import com.jd.twitterclonebackend.mapper.UserMapper;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserMapperTest extends UnitTestInitData {

    @InjectMocks
    private UserMapper underTest;

    @Mock
    private FileService fileService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JsonMapper jsonMapper;
    @Mock
    private FollowerMapper followerMapper;

    @Test
    @DisplayName(value = "Should map UserResponseDto from UserEntity")
    void should_mapFromUserEntity_toUserResponseDto() {
        // given
        UserEntity userEntity = initUserEntity();

        // when
        var result = underTest.mapFromEntityToDto(userEntity);

        // then
        assertAll(
                () -> {
                    assertEquals(userEntity.getId(), result.getId());
                    assertEquals(userEntity.getName(), result.getName());
                    assertEquals(userEntity.getUsername(), result.getUsername());
                    assertEquals(userEntity.getTweets().size(), result.getTweetNo());
                    assertEquals(userEntity.getFollowingNo(), result.getFollowingNo());
                    assertEquals(userEntity.getFollowerNo(), result.getFollowerNo());
                    assertEquals(userEntity.getPublishedNotifications().size(), result.getNotificationNo());
                    assertEquals(userEntity.getProfilePicture(), result.getUserProfilePicture());
                    assertEquals(userEntity.getBackgroundPicture(), result.getUserBackgroundPicture());
                    assertEquals(userEntity.getDescription(), result.getDescription());
                }
        );

    }

    @Test
    @DisplayName(value = "Should map UserEntity from Update Request")
    void should_mapFromUserRequestDto_toUserEntity() {
        // given
        UserEntity userEntity = initUserEntity();
        String userDetailsRequestJson = "";
        UserDetailsRequestDto userDetailsRequestDto = initUserRequestDto();
        MultipartFile profileImageFile = new MockMultipartFile("byteUpdate", "byteUpdate".getBytes());
        MultipartFile backgroundImageFile = new MockMultipartFile("byteUpdate", "byteUpdate".getBytes());

        // when
        when(passwordEncoder.encode(any()))
                .thenReturn(userDetailsRequestDto.getPassword());
        when(fileService.convertFileToByteArray(any()))
                .thenReturn("byteUpdate".getBytes());
        when(jsonMapper.mapFromJsonToDto(any(), any()))
                .thenReturn(userDetailsRequestDto);

        var result = underTest.mapFromDtoToEntity(
                userEntity,
                userDetailsRequestJson,
                profileImageFile,
                backgroundImageFile
        );

        // then
        assertAll(
                () -> {
                    assertEquals(userDetailsRequestDto.getName(), result.getName());
                    assertEquals(userDetailsRequestDto.getEmailAddress(), result.getEmailAddress());
                    assertEquals(userDetailsRequestDto.getPhoneNumber(), result.getPhoneNumber());
                    assertEquals(passwordEncoder.encode(userDetailsRequestDto.getPassword()),
                            result.getPassword());
                    assertEquals(userDetailsRequestDto.getDescription(), userEntity.getDescription());
                    assertEquals(fileService.convertFileToByteArray(profileImageFile),
                            result.getProfilePicture());
                    assertEquals(fileService.convertFileToByteArray(backgroundImageFile),
                            result.getBackgroundPicture());
                }
        );
    }

}