package com.jd.twitterclonebackend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfirmationDto {

    private String message;
}
