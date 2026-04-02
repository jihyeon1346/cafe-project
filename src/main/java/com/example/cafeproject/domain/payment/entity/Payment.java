package com.example.cafeproject.domain.payment.entity;

import com.example.cafeproject.common.entity.BaseEntity;
import com.example.cafeproject.domain.payment.consts.PaymentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table (name = "payments")
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@Getter
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private LocalDateTime payAt;

    public static Payment success(BigDecimal amount, Long orderId) {
        Payment payment = new Payment();
        payment.amount = amount;
        payment.status = PaymentStatus.SUCCESS;
        payment.orderId = orderId;
        payment.payAt = LocalDateTime.now();
        return payment;
    }
}
