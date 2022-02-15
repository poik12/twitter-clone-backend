package com.jd.twitterclonebackend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String name;

    private String emailAddress;

    private String username;

    private String password;

    private String phoneNumber;

}
