package com.jd.twitterclonebackend.controller.handler;

import com.jd.twitterclonebackend.controller.CommentController;
import com.jd.twitterclonebackend.controller.handler.dtos.ErrorMessageDto;
import com.jd.twitterclonebackend.exception.CommentException;
import com.jd.twitterclonebackend.exception.PostException;
import com.jd.twitterclonebackend.exception.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = CommentController.class)
public class CommentControllerExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ErrorMessageDto handleException(UserException userException) {
        return ErrorMessageDto.builder()
                .status(userException.getStatus())
                .message(userException.getMessage())
                .build();
    }

    @ExceptionHandler(value = CommentException.class)
    public ErrorMessageDto handleException(CommentException commentException) {
        return ErrorMessageDto.builder()
                .status(commentException.getStatus())
                .message(commentException.getMessage())
                .build();
    }

    @ExceptionHandler(value = PostException.class)
    public ErrorMessageDto handleException(PostException postException) {
        return ErrorMessageDto.builder()
                .status(postException.getStatus())
                .message(postException.getMessage())
                .build();
    }



}
