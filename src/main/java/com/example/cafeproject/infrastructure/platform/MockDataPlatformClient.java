package com.example.cafeproject.infrastructure.platform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class MockDataPlatformClient implements DataPlatformClient {

    @Override
    public void send(Long userId, Long productId, BigDecimal amount) {
        log.info("[DataPlatform] 주문 데이터 전송 - userId={}, productId={}, amount={}",
                userId, productId, amount);
    }
}
