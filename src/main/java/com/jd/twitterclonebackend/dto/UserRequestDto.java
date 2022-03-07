package com.jd.twitterclonebackend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
public class UserRequestDto {

    String name;
    String username;
//    private String description;
    String emailAddress;
    String phoneNumber;
    String password;

}
