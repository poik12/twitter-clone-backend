package com.jd.twitterclonebackend.exception.enums;

public enum InvalidPostEnum {

    POST_NOT_FOUND("Post not found with id: "),
    POST_FOR_COMMENT_NOT_FOUND("Post was not found for comment with id: ");


    private final String message;

    InvalidPostEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
