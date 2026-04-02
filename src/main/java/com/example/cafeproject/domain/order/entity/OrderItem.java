package com.example.cafeproject.domain.order.entity;

import com.example.cafeproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table (name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    public static OrderItem create(Long productId, Long orderId, Long quantity, BigDecimal unitPrice) {
        OrderItem item = new OrderItem();
        item.productId = productId;
        item.orderId = orderId;
        item.quantity = quantity;
        item.unitPrice = unitPrice;
        return item;
    }
}
