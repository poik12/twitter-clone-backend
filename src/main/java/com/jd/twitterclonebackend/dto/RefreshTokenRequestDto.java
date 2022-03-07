package com.jd.twitterclonebackend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class RefreshTokenRequestDto {

    @NotBlank
    String refreshToken;
    String username;

}
