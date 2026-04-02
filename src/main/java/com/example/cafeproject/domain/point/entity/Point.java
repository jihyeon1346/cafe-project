package com.example.cafeproject.domain.point.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "points")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private Long userId;

    @Column
    private LocalDateTime updatedAt;

    public Point(Long userId) {
        this.userId = userId;
        this.balance = BigDecimal.ZERO;
        this.updatedAt = LocalDateTime.now();
    }

    public void charge(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void use(BigDecimal amount) {
        if(this.balance.subtract(amount).compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }
        this.balance = this.balance.subtract(amount);
    }
}
