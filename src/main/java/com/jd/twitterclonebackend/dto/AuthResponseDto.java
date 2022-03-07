package com.jd.twitterclonebackend.dto;

import lombok.*;

@Value
@Builder
public class AuthResponseDto {

    String username;
    String authenticationToken;
    String expiresAt;
    String refreshToken;

}
