package com.example.cafeproject.common.model.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedEvent {

    private Long orderId;
    private Long userId;
    private Long productId;
    private Long quantity;
    private BigDecimal paymentAmount;
    private String paidAt;
}
