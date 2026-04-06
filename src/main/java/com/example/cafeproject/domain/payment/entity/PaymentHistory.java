package com.example.cafeproject.domain.payment.entity;

import com.example.cafeproject.common.model.kafka.event.OrderCompletedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "payment_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private BigDecimal paymentAmount;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    private PaymentHistory(Long orderId, Long userId, Long productId,
                           Long quantity, BigDecimal paymentAmount, LocalDateTime paidAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.paymentAmount = paymentAmount;
        this.paidAt = paidAt;
    }

    public static PaymentHistory from(OrderCompletedEvent event) {
        return new PaymentHistory(
                event.getOrderId(),
                event.getUserId(),
                event.getProductId(),
                event.getQuantity(),
                event.getPaymentAmount(),
                LocalDateTime.parse(event.getPaidAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}