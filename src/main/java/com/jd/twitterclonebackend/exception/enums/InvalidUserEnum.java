package com.jd.twitterclonebackend.exception.enums;

public enum InvalidUserEnum {

    USER_NOT_FOUND_WITH_USERNAME("user not found with username: "),
    USER_NOT_FOUND_WITH_ID("user not found with id: ");

    private final String message;

    InvalidUserEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}
