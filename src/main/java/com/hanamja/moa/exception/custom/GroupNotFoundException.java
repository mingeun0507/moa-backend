package com.hanamja.moa.exception.custom;

import org.springframework.http.HttpStatus;

public class GroupNotFoundException extends CustomException {
    public GroupNotFoundException(String message) {
        super(message);
    }

    public GroupNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
