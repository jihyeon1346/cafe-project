package com.example.cafeproject.domain.point_transaction.entity;

import com.example.cafeproject.domain.point_transaction.consts.PointTransactionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "point_transactions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointTransactionStatus status;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Long orderId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private PointTransaction(BigDecimal amount, PointTransactionStatus status,
                             BigDecimal balance, Long userId, Long orderId) {
        this.amount = amount;
        this.status = status;
        this.balance = balance;
        this.userId = userId;
        this.orderId = orderId;
        this.createdAt = LocalDateTime.now();
    }

    public static PointTransaction charge(BigDecimal amount, BigDecimal balanceAfter, Long userId) {
        return new PointTransaction(amount, PointTransactionStatus.CHARGE, balanceAfter, userId, null);
    }

    public static PointTransaction use(BigDecimal amount, BigDecimal balanceAfter, Long userId, Long orderId) {
        return new PointTransaction(amount, PointTransactionStatus.USE, balanceAfter, userId, orderId);
    }

}
