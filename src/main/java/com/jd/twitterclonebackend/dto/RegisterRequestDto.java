package com.jd.twitterclonebackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    private String name;
    private String emailAddress;
    private String username;
    private String password;
    private String phoneNumber;

}
