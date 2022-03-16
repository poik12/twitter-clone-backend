package com.jd.twitterclonebackend.entity.enums;

public enum NotificationType {

    FOLLOWER("follower"),
    MESSAGE("message"),
    TWEET("tweet"),
    COMMENT("comment"),
    LIKE("like");

    private final String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
