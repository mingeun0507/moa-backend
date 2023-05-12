package com.hanamja.moa.exception.custom;

import org.springframework.http.HttpStatus;

public class S3UploadException extends CustomException {
    public S3UploadException(String message) {
        super(message);
    }

    public S3UploadException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
