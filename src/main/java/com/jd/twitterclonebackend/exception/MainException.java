package com.jd.twitterclonebackend.exception;

public class MainException extends RuntimeException {

    private final String errorCode;

    public MainException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
