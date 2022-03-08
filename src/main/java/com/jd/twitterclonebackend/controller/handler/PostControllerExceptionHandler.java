package com.jd.twitterclonebackend.controller.handler;

import com.jd.twitterclonebackend.controller.PostController;
import com.jd.twitterclonebackend.controller.handler.dtos.ErrorMessage;
import com.jd.twitterclonebackend.exception.PostException;
import com.jd.twitterclonebackend.exception.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = PostController.class)
public class PostControllerExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ErrorMessage handleException(UserException userException) {
        return ErrorMessage.builder()
                .status(userException.getStatus())
                .message(userException.getMessage())
                .build();
    }

    @ExceptionHandler(value = PostException.class)
    public ErrorMessage handleException(PostException postException) {
        return ErrorMessage.builder()
                .status(postException.getStatus())
                .message(postException.getMessage())
                .build();
    }

}
