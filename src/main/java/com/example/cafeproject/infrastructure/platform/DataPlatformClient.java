package com.example.cafeproject.infrastructure.platform;

import java.math.BigDecimal;

public interface DataPlatformClient {
    void send(Long userId, Long productId, BigDecimal amount);
}
