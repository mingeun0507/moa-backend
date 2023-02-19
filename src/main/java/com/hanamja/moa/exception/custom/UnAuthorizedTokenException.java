package com.hanamja.moa.exception.custom;

import org.springframework.http.HttpStatus;

public class UnAuthorizedTokenException extends CustomException{
    public UnAuthorizedTokenException(String message) {
        super(message);
    }

    public UnAuthorizedTokenException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
