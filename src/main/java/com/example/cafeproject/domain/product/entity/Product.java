package com.example.cafeproject.domain.product.entity;

import com.example.cafeproject.common.entity.BaseEntity;
import com.example.cafeproject.common.exception.InsufficientStockException;
import com.example.cafeproject.domain.product.consts.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    public Product(String name, BigDecimal price, Long quantity, ProductStatus status) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    public boolean isOnSale() {
        return this.status == ProductStatus.ON_SALE;
    }

    public void deductQuantity(long count) {
        if (this.quantity < count) {
            throw new InsufficientStockException("재고가 부족합니다.");
        }
        this.quantity -= count;
        if (this.quantity == 0) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }
}
