package com.example.cafeproject.domain.order.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CreateOrderResponse {
    private final Long orderId;
    private final String orderNum;
    private final List<String> productNames;
    private final BigDecimal totalAmount;
    private final BigDecimal remainingBalance;
    private final LocalDateTime createdAt;

    public CreateOrderResponse(Long orderId, String orderNum, List<String> productNames, BigDecimal totalAmount, BigDecimal remainingBalance, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderNum = orderNum;
        this.productNames = productNames;
        this.totalAmount = totalAmount;
        this.remainingBalance = remainingBalance;
        this.createdAt = createdAt;
    }
}
