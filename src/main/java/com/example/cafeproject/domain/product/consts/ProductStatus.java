package com.example.cafeproject.domain.product.consts;

public enum ProductStatus {
    ON_SALE("판매중"),
    SOLD_OUT("품절"),
    DISCONTINUED("단종");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }
}
