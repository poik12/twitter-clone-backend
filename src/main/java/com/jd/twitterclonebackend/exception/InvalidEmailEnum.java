package com.jd.twitterclonebackend.exception;

public enum InvalidEmailEnum {

    SENDING_EMAIL_ERROR("Exception occurred when sending mail to: ");

    private final String message;

    InvalidEmailEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
