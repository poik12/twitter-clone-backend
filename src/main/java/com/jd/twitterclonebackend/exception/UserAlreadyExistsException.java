package com.jd.twitterclonebackend.exception;

import com.jd.twitterclonebackend.enums.AuthenticationMessageEnum;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(AuthenticationMessageEnum authenticationMessageEnum,
                                      String data) {
        super(authenticationMessageEnum.getMessage() + data);
    }
}
