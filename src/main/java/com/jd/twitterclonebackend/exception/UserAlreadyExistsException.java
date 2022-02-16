package com.jd.twitterclonebackend.exception;

import com.jd.twitterclonebackend.exception.enums.InvalidAuthenticationEnum;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(InvalidAuthenticationEnum invalidAuthenticationEnum,
                                      String data) {
        super(invalidAuthenticationEnum.getMessage() + data);
    }
}
