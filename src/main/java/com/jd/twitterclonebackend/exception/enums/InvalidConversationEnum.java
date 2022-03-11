package com.jd.twitterclonebackend.exception.enums;

public enum InvalidConversationEnum {

    CONVERSATION_NOT_FOUND_WITH_ID("conversation not found with id: ");

    private final String message;

    InvalidConversationEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
