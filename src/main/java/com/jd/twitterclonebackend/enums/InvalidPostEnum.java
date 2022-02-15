package com.jd.twitterclonebackend.enums;

public enum InvalidPostEnum {

    POST_NOT_FOUND("Post not found with id: ");


    private final String message;

    InvalidPostEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
