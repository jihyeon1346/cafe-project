package com.example.cafeproject.domain.order.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PopularProductDto {
    private final Long productId;
    private final String productName;
    private final BigDecimal price;
    private final Long orderCount;

    public PopularProductDto(Long productId, String productName, BigDecimal price, Long orderCount) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.orderCount = orderCount;
    }
}
