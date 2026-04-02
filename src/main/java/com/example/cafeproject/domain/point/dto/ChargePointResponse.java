package com.example.cafeproject.domain.point.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ChargePointResponse {
    private final Long userId;
    private final BigDecimal chargedAmount;
    private final BigDecimal balance;
    private final LocalDateTime updatedAt;

    public ChargePointResponse(Long userId, BigDecimal chargedAmount, BigDecimal balance, LocalDateTime updatedAt) {
        this.userId = userId;
        this.chargedAmount = chargedAmount;
        this.balance = balance;
        this.updatedAt = updatedAt;
    }
}
