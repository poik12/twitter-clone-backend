package com.jd.twitterclonebackend.controller.handler;

import com.jd.twitterclonebackend.controller.AuthController;
import com.jd.twitterclonebackend.controller.handler.dto.ExceptionMessageDto;
import com.jd.twitterclonebackend.exception.EmailException;
import com.jd.twitterclonebackend.exception.TokenException;
import com.jd.twitterclonebackend.exception.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthControllerExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ExceptionMessageDto handleException(UserException userException) {
        return ExceptionMessageDto.builder()
                .status(userException.getStatus())
                .message(userException.getMessage())
                .build();
    }

    @ExceptionHandler(value = EmailException.class)
    public ExceptionMessageDto handleException(EmailException emailException) {
        return ExceptionMessageDto.builder()
                .status(emailException.getStatus())
                .message(emailException.getMessage())
                .build();
    }

    @ExceptionHandler(value = TokenException.class)
    public ExceptionMessageDto handleException(TokenException tokenException) {
        return ExceptionMessageDto.builder()
                .status(tokenException.getStatus())
                .message(tokenException.getMessage())
                .build();
    }

}
