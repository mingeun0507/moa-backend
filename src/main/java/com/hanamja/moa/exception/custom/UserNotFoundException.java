package com.hanamja.moa.exception.custom;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
