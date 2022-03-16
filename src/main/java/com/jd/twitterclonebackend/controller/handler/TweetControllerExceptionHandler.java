package com.jd.twitterclonebackend.controller.handler;

import com.jd.twitterclonebackend.controller.TweetController;
import com.jd.twitterclonebackend.controller.handler.dto.ExceptionMessageDto;
import com.jd.twitterclonebackend.exception.TweetException;
import com.jd.twitterclonebackend.exception.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = TweetController.class)
public class TweetControllerExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ExceptionMessageDto handleException(UserException userException) {
        return ExceptionMessageDto.builder()
                .status(userException.getStatus())
                .message(userException.getMessage())
                .build();
    }

    @ExceptionHandler(value = TweetException.class)
    public ExceptionMessageDto handleException(TweetException tweetException) {
        return ExceptionMessageDto.builder()
                .status(tweetException.getStatus())
                .message(tweetException.getMessage())
                .build();
    }

}
