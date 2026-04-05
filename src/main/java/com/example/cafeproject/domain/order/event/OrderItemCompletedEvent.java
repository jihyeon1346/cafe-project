package com.example.cafeproject.domain.order.event;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItemCompletedEvent {

    private final Long orderId;
    private final Long userId;
    private final Long productId;
    private final Long quantity;
    private final BigDecimal paymentAmount;
    private final String paidAt;

    public OrderItemCompletedEvent(Long orderId, Long userId, Long productId,
                                   Long quantity, BigDecimal paymentAmount, String paidAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.paymentAmount = paymentAmount;
        this.paidAt = paidAt;
    }
}