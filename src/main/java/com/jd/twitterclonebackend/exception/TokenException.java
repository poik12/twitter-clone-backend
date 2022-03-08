package com.jd.twitterclonebackend.exception;

import org.springframework.http.HttpStatus;

public class TokenException extends RuntimeException {

    private final HttpStatus status;

    public TokenException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
