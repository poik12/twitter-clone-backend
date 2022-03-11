package com.jd.twitterclonebackend.controller.handler;

import com.jd.twitterclonebackend.controller.ConversationController;
import com.jd.twitterclonebackend.controller.handler.dto.ExceptionMessageDto;
import com.jd.twitterclonebackend.exception.ConversationException;
import com.jd.twitterclonebackend.exception.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ConversationController.class)
public class ConversationControllerExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ExceptionMessageDto handleException(UserException userException) {
        return ExceptionMessageDto.builder()
                .status(userException.getStatus())
                .message(userException.getMessage())
                .build();
    }

    @ExceptionHandler(value = ConversationException.class)
    public ExceptionMessageDto handleException(ConversationException conversationException) {
        return ExceptionMessageDto.builder()
                .status(conversationException.getStatus())
                .message(conversationException.getMessage())
                .build();
    }

}
