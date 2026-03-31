package com.example.cafeproject.domain.product.dto;

import com.example.cafeproject.domain.product.consts.ProductStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class GetProductResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final ProductStatus status;
    private final LocalDateTime createdAt;

    public GetProductResponse(Long id, String name, BigDecimal price, ProductStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
    }
}
