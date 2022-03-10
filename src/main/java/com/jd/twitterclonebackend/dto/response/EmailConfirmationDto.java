package com.jd.twitterclonebackend.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfirmationDto {

    private String message;
}
