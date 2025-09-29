package com.hr.examportal.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{

    private HttpStatus statusCode;
    public CustomException(String message) {
        super(message);
        statusCode = HttpStatus.BAD_REQUEST;
    }
    public CustomException(String message, HttpStatus status) {
        super(message);
        statusCode = status;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
