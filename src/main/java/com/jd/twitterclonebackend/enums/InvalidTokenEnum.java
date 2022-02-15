package com.jd.twitterclonebackend.enums;

public enum InvalidTokenEnum {

    INVALID_ACCESS_TOKEN("Invalid access Token"),
    INVALID_REFRESH_TOKEN("Invalid refresh Token"),
    INVALID_VERIFICATION_TOKEN("Invalid verification Token"),
    REFRESH_TOKEN_EXPIRED("Refresh token has expired"),
    MISSING_REFRESH_TOKEN("Refresh token is missing"),
    EMAIL_ALREADY_CONFIRMED("Email already confirmed"),
    USER_ALREADY_CONFIRMED("User is already confirmed");

    private final String message;

    InvalidTokenEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
