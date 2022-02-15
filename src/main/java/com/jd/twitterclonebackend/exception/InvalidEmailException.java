package com.jd.twitterclonebackend.exception;

public class InvalidEmailException extends MainException {

    public InvalidEmailException(String message, String emailRecipient) {
        super(message, emailRecipient);
    }

}
