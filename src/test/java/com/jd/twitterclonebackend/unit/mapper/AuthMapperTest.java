package com.jd.twitterclonebackend.unit.mapper;

import com.jd.twitterclonebackend.dto.RegisterRequestDto;
import com.jd.twitterclonebackend.mapper.AuthMapper;
import com.jd.twitterclonebackend.service.FileService;
import com.jd.twitterclonebackend.unit.InitUnitTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthMapperTest extends InitUnitTestData {

    @InjectMocks
    private AuthMapper authMapper;

    @Mock
    private FileService fileService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void should_mapFromRegisterRequestDto_ToUserEntity() {
        // given
        RegisterRequestDto registerRequestDto = initRegisterRequestDto();

        when(passwordEncoder.encode(any()))
                .thenReturn(registerRequestDto.getPassword());

        // when
        var result = authMapper.mapFromDtoToEntity(registerRequestDto);

        // then
        assertAll(
                () -> {
                    assertEquals(registerRequestDto.getName(), result.getName());
                    assertEquals(registerRequestDto.getEmailAddress(), result.getEmailAddress());
                    assertEquals(registerRequestDto.getUsername(), result.getUsername());
                    assertEquals(registerRequestDto.getPassword(), result.getPassword());
                    assertEquals(registerRequestDto.getPhoneNumber(), result.getPhoneNumber());
                    assertEquals(false, result.getEnabled());
                    // TODO: ADD THE REST FIELDS
                }
        );

    }
}