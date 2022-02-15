package com.jd.twitterclonebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserRequest {

    private String name;

    private String username;

//    private String description;

    private String emailAddress;

    private String phoneNumber;

    private String password;

}
