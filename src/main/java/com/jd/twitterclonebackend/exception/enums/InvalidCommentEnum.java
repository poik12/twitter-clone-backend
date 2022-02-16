package com.jd.twitterclonebackend.exception.enums;

public enum InvalidCommentEnum {

    COMMENT_NOT_FOUND_WITH_ID("Comment was not found with id: ");

    private final String message;

    InvalidCommentEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
