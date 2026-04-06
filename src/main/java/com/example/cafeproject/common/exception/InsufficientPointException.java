package com.example.cafeproject.common.exception;

import org.springframework.http.HttpStatus;

public class InsufficientPointException extends BusinessException {
    public InsufficientPointException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
