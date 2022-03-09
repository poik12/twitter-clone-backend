package com.jd.twitterclonebackend.controller.handler;

import com.jd.twitterclonebackend.controller.AuthController;
import com.jd.twitterclonebackend.controller.handler.dtos.ErrorMessageDto;
import com.jd.twitterclonebackend.exception.EmailException;
import com.jd.twitterclonebackend.exception.TokenException;
import com.jd.twitterclonebackend.exception.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthControllerExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ErrorMessageDto handleException(UserException userException) {
        return ErrorMessageDto.builder()
                .status(userException.getStatus())
                .message(userException.getMessage())
                .build();
    }

    @ExceptionHandler(value = EmailException.class)
    public ErrorMessageDto handleException(EmailException emailException) {
        return ErrorMessageDto.builder()
                .status(emailException.getStatus())
                .message(emailException.getMessage())
                .build();
    }

    @ExceptionHandler(value = TokenException.class)
    public ErrorMessageDto handleException(TokenException tokenException) {
        return ErrorMessageDto.builder()
                .status(tokenException.getStatus())
                .message(tokenException.getMessage())
                .build();
    }

}
