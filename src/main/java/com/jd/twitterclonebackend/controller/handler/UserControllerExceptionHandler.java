package com.jd.twitterclonebackend.controller.handler;

import com.jd.twitterclonebackend.controller.UserController;
import com.jd.twitterclonebackend.controller.handler.dtos.ErrorMessageDto;
import com.jd.twitterclonebackend.exception.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = UserController.class)
public class UserControllerExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ErrorMessageDto handleException(UserException userException) {
        return ErrorMessageDto.builder()
                .status(userException.getStatus())
                .message(userException.getMessage())
                .build();
    }

}
