package com.example.cafeproject.domain.order.entity;

import com.example.cafeproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String orderNum;

    @Column(nullable = false)
    private BigDecimal amount;

    private LocalDateTime deletedAt;

    public Order(Long userId, BigDecimal amount) {
        this.userId = userId;
        this.amount = amount;
        this.orderNum = UUID.randomUUID().toString();
    }

    public void softDelete(){
        this.deletedAt = LocalDateTime.now();
    }
}
