package com.jd.twitterclonebackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private String name;
    private String username;
//     private String description;
    private String emailAddress;
    private String phoneNumber;
    private String password;

}
