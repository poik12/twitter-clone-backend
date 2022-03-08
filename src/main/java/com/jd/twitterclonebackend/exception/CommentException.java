package com.jd.twitterclonebackend.exception;

import org.springframework.http.HttpStatus;

public class CommentException extends RuntimeException {

    private final HttpStatus status;

    public CommentException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
