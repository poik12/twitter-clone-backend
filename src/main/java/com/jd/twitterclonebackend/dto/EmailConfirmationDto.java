package com.jd.twitterclonebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmailConfirmationDto {

    String message;
}
