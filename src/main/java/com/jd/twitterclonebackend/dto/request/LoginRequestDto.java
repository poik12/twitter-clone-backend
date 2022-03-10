package com.jd.twitterclonebackend.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    String username;
    String password;

}
