package com.example.cafeproject.domain.product.dto;

import lombok.Getter;

@Getter
public class PopularMenuDto {
    private final Long productId;
    private final Long orderCount;

    public PopularMenuDto(Long productId, Long orderCount) {
        this.productId = productId;
        this.orderCount = orderCount;
    }
}
