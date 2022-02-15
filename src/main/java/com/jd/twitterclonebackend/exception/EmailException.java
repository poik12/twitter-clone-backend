package com.jd.twitterclonebackend.exception;

public class EmailException extends MainException {

    public EmailException(String message, String emailRecipient) {
        super(message, emailRecipient);
    }

}
