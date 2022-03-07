package com.jd.twitterclonebackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
@Builder
public class LoginRequestDto {

    String username;
    String password;

}
