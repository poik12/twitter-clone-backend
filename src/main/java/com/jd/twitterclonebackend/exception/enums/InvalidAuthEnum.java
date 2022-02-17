package com.jd.twitterclonebackend.exception.enums;

public enum InvalidAuthEnum {

    AUTHENTICATION_FAILED("Something went wrong while attempting authentication: ");

    private final String message;

    InvalidAuthEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
