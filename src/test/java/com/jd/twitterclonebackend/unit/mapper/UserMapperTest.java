package com.jd.twitterclonebackend.unit.mapper;

import com.jd.twitterclonebackend.dto.request.UserDetailsRequestDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.JsonMapper;
import com.jd.twitterclonebackend.mapper.UserMapper;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.service.PostService;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
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
    private UserMapper userMapper;

    @Mock
    private PostService postService;
    @Mock
    private FileService fileService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JsonMapper jsonMapper;

    @Test
    void should_mapFromUserEntity_toUserResponseDto() {
        // given
        UserEntity userEntity = initUserEntity();

        // when
        var result = userMapper.mapFromEntityToUserDto(userEntity);
        System.out.println(result);

        // then
        assertAll(
                () -> {
                    assertEquals(userEntity.getId(), result.getId());
                    assertEquals(userEntity.getName(), result.getName());
                    assertEquals(userEntity.getUsername(), result.getUsername());
                    assertEquals(userEntity.getTweetNo(), result.getTweetNo());
                    assertEquals(userEntity.getFollowingNo(), result.getFollowingNo());
                    assertEquals(userEntity.getFollowerNo(), result.getFollowerNo());
                    assertEquals(userEntity.getProfilePicture(), result.getUserProfilePicture());
                    assertEquals(userEntity.getBackgroundPicture(), result.getUserBackgroundPicture());
                    assertEquals(userEntity.getDescription(), result.getDescription());
                }
        );

    }

    @Test
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
        when(fileService.convertImageFileToByteArray(any()))
                .thenReturn("byteUpdate".getBytes());
        when(jsonMapper.mapFromJsonToDto(any(), any()))
                .thenReturn(userDetailsRequestDto);

        var result = userMapper.mapFromUserDtoToEntity(
                userEntity,
                userDetailsRequestJson,
                profileImageFile,
                backgroundImageFile
        );
        System.out.println(result);

        // then
        assertAll(
                () -> {
                    assertEquals(userDetailsRequestDto.getName(), result.getName());
                    assertEquals(userDetailsRequestDto.getUsername(), result.getUsername());
                    assertEquals(userDetailsRequestDto.getEmailAddress(), result.getEmailAddress());
                    assertEquals(userDetailsRequestDto.getPhoneNumber(), result.getPhoneNumber());
                    assertEquals(userDetailsRequestDto.getPassword(), result.getPassword());
//                    assertEquals(profileImageFile, result.getProfilePicture());
//                    assertEquals(backgroundImageFile, result.getBackgroundPicture());
                    // TODO: Change image mapping
                }
        );
    }

}