package com.jd.twitterclonebackend.exception.enums;

public enum InvalidAuthenticationEnum {

    USER_ALREADY_EXISTS("user already exists with: "),
    USER_NOT_FOUND("user not found with: "),
    INVALID_USERNAME_OR_PASSWORD("invalid username or password"),
    EMAIL_CONFIRMED("Email has been confirmed");

    private final String message;

    InvalidAuthenticationEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}