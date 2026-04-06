package com.example.cafeproject.domain.product.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class PopularProductResponse {
    private final int rank;
    private final Long id;
    private final String productName;
    private final BigDecimal price;
    private final Long orderCount;
    private final LocalDateTime createdAt;

    public PopularProductResponse(int rank, Long id, String productName, BigDecimal price, Long orderCount, LocalDateTime createdAt) {
        this.rank = rank;
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.orderCount = orderCount;
        this.createdAt = createdAt;
    }
}
