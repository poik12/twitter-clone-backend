package com.jd.twitterclonebackend.controller.handler.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageDto {

    private HttpStatus status;
    private String message;
}
