package com.jd.twitterclonebackend.exception.enums;

public enum InvalidTweetEnum {

    TWEET_NOT_FOUND_WITH_ID("Tweet not found with id: "),
    TWEET_FOR_COMMENT_NOT_FOUND("Tweet was not found for comment with id: ");


    private final String message;

    InvalidTweetEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
