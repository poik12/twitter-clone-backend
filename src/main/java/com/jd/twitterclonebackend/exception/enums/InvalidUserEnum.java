package com.jd.twitterclonebackend.exception.enums;

public enum InvalidUserEnum {

    USER_NOT_FOUND_WITH_USERNAME("user not found with username: "),
    USER_NOT_FOUND_WITH_ID("user not found with id: "),
    AUTHENTICATION_FAILED("Something went wrong while attempting authentication: "),

    USER_ALREADY_EXISTS_WITH_USERNAME("user already exists with username: "),
    USER_ALREADY_EXISTS_WITH_EMAIL("user already exists with email: "),
    INVALID_USERNAME_OR_PASSWORD("invalid username or password"),
    USER_IS_EMPTY("user entity is empty");

    private final String message;

    InvalidUserEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
