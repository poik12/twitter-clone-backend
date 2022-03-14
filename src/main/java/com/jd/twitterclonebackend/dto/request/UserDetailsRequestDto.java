package com.jd.twitterclonebackend.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsRequestDto {

    private String name;
    private String username;
    private String description;
    private String emailAddress;
    private String phoneNumber;
    private String password;

}
