package com.example.cafeproject.common.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
