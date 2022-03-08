package com.jd.twitterclonebackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {

    private String username;
    private String authenticationToken;
    private String expiresAt;
    private String refreshToken;

}
