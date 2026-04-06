package com.example.cafeproject.domain.point_transaction.consts;

public enum PointTransactionStatus {

    CHARGE("포인트 충전"),
    USE("포인트 사용");

    private final String description;

    PointTransactionStatus(String description) {
        this.description = description;
    }
}
