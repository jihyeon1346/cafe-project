package com.example.cafeproject.common.exception;

import org.springframework.http.HttpStatus;

public class ProductNotOnSaleException extends BusinessException {
    public ProductNotOnSaleException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
