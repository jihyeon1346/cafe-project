package com.example.cafeproject.domain.payment.consts;

public enum PaymentStatus {
    PENDING("결제 대기"),
    SUCCESS("결제 완료"),
    FAILED("결제 실패");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
