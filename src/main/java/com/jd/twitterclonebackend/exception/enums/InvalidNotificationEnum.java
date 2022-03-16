package com.jd.twitterclonebackend.exception.enums;

public enum InvalidNotificationEnum {

    NOTIFICATION_NOT_FOUND_WITH_ID("Notification not found with id: ");

    private final String message;

    InvalidNotificationEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
