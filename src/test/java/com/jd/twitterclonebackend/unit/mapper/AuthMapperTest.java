package com.jd.twitterclonebackend.unit.mapper;

import com.jd.twitterclonebackend.dto.request.RegisterRequestDto;
import com.jd.twitterclonebackend.entity.UserEntity;
import com.jd.twitterclonebackend.mapper.AuthMapper;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.unit.UnitTestInitData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthMapperTest extends UnitTestInitData {

    @InjectMocks
    private AuthMapper underTest;

    @Mock
    private FileService fileService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void should_mapFromRegisterRequestDto_ToUserEntity() {
        // given
        RegisterRequestDto registerRequestDto = initRegisterRequestDto();

        UserEntity mock = mock(UserEntity.class);

        // when
        when(passwordEncoder.encode(any()))
                .thenReturn(registerRequestDto.getPassword());
        when(fileService.convertFilePathToByteArray(any()))
                .thenReturn(null);
        when(fileService.convertFilePathToByteArray(any()))
                .thenReturn(null);

        var result = underTest.mapFromDtoToEntity(registerRequestDto);
        System.out.println(result);

        // then
        assertAll(
                () -> {
                    assertEquals(registerRequestDto.getName(), result.getName());
                    assertEquals(registerRequestDto.getEmailAddress(), result.getEmailAddress());
                    assertEquals(registerRequestDto.getUsername(), result.getUsername());
                    assertEquals(registerRequestDto.getPassword(), result.getPassword());
                    assertEquals(registerRequestDto.getPhoneNumber(), result.getPhoneNumber());
                    assertEquals(false, result.getEnabled());
                    assertEquals(0L, result.getFollowerNo());
                    assertEquals(0L, result.getFollowingNo());
                    assertEquals(0L, result.getTweetNo());
                    assertEquals(null, result.getDescription());
                    // TODO: ADD THE REST FIELDS
                }
        );

    }
}