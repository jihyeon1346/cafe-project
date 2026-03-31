package com.example.cafeproject.domain.point.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ChargePointResponse(
        Long userId,
        BigDecimal chargedAmount,
        BigDecimal balance,
        LocalDateTime updatedAt
) {
}
