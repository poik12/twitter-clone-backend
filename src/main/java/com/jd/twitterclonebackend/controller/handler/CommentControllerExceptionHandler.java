package com.jd.twitterclonebackend.controller.handler;

import com.jd.twitterclonebackend.controller.CommentController;
import com.jd.twitterclonebackend.controller.handler.dto.ExceptionMessageDto;
import com.jd.twitterclonebackend.exception.CommentException;
import com.jd.twitterclonebackend.exception.PostException;
import com.jd.twitterclonebackend.exception.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = CommentController.class)
public class CommentControllerExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ExceptionMessageDto handleException(UserException userException) {
        return ExceptionMessageDto.builder()
                .status(userException.getStatus())
                .message(userException.getMessage())
                .build();
    }

    @ExceptionHandler(value = CommentException.class)
    public ExceptionMessageDto handleException(CommentException commentException) {
        return ExceptionMessageDto.builder()
                .status(commentException.getStatus())
                .message(commentException.getMessage())
                .build();
    }

    @ExceptionHandler(value = PostException.class)
    public ExceptionMessageDto handleException(PostException postException) {
        return ExceptionMessageDto.builder()
                .status(postException.getStatus())
                .message(postException.getMessage())
                .build();
    }



}
