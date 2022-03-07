package com.jd.twitterclonebackend.dto;

import lombok.*;

@Value
@Builder
public class RegisterRequestDto {

    String name;
    String emailAddress;
    String username;
    String password;
    String phoneNumber;

}
