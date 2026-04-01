package com.example.cafeproject.domain.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateOrderResponse(
        Long orderId,
        String orderNum,
        String productName,
        BigDecimal amount,
        BigDecimal remainingBalance,
        LocalDateTime createdAt
) {
}
