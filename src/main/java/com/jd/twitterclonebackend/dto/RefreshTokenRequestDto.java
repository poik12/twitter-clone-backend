package com.jd.twitterclonebackend.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDto {

    @NotBlank
    private String refreshToken;
    private String username;

}
