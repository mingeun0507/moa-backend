package com.hanamja.moa.exception.handler;

import com.hanamja.moa.exception.ApiException;
import com.hanamja.moa.exception.custom.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ApiException> handlerCustomException(CustomException e) {
        log.error("Unexpected Exception occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiException
                        .builder()
                        .message(e.getMessage())
                        .httpStatus(e.getHttpStatus())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
