package com.example.cafeproject.domain.product.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PopularProductResponse {
    private final int rank;
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public PopularProductResponse(int rank, Long id, String name, BigDecimal price) {
        this.rank = rank;
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
