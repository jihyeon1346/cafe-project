package com.example.cafeproject.domain.point.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ChargePointRequest(

        @NotNull(message = "사용자 ID는 필수입니다.")
        Long userId,

        @NotNull(message = "충전 금액은 필수입니다.")
        @Positive(message = "충전 금액은 1원 이상이어야 합니다.")
        BigDecimal amount
) {
}
