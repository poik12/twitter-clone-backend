package com.jd.twitterclonebackend.controller.handler;

import com.jd.twitterclonebackend.controller.AuthController;
import com.jd.twitterclonebackend.controller.handler.dtos.ErrorMessage;
import com.jd.twitterclonebackend.exception.EmailException;
import com.jd.twitterclonebackend.exception.TokenException;
import com.jd.twitterclonebackend.exception.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthControllerExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ErrorMessage handleException(UserException userException) {
        return ErrorMessage.builder()
                .status(userException.getStatus())
                .message(userException.getMessage())
                .build();
    }

    @ExceptionHandler(value = EmailException.class)
    public ErrorMessage handleException(EmailException emailException) {
        return ErrorMessage.builder()
                .status(emailException.getStatus())
                .message(emailException.getMessage())
                .build();
    }

    @ExceptionHandler(value = TokenException.class)
    public ErrorMessage handleException(TokenException tokenException) {
        return ErrorMessage.builder()
                .status(tokenException.getStatus())
                .message(tokenException.getMessage())
                .build();
    }

}
