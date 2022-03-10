package com.jd.twitterclonebackend.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDto {

    @NotBlank
    private String refreshToken;
    private String username;

}
