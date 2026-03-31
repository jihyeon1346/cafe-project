package com.example.cafeproject.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidAmountException extends BusinessException {

    public InvalidAmountException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
