package com.jd.twitterclonebackend.dto;

import lombok.Data;

@Data
public class UserRequestDto {

    private String name;

    private String username;

//    private String description;

    private String emailAddress;

    private String phoneNumber;

    private String password;

}
