package com.jd.twitterclonebackend.exception;

public enum InvalidTokenEnum {

    INVALID_ACCESS_TOKEN("Invalid access Token"),
    INVALID_REFRESH_TOKEN("Invalid refresh Token"),
    INVALID_VERIFICATION_TOKEN("Invalid verification Token"),
    REFRESH_TOKEN_EXPIRED("Refresh token has expired"),
    MISSING_REFRESH_TOKEN("Refresh token is missing");

    private final String message;

    InvalidTokenEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
