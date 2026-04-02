package com.example.cafeproject.infrastructure.platform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 데이터 수집 플랫폼 Mock 구현체.
 *
 * 현재는 로그 출력으로 전송을 대체합니다.
 * 실제 환경에서는 Kafka Producer, HTTP 클라이언트 등으로 교체 가능하도록
 * DataPlatformClient 인터페이스로 추상화했습니다.
 */
@Slf4j
@Component
public class MockDataPlatformClient implements DataPlatformClient {

    @Override
    public void send(Long userId, Long productId, BigDecimal amount) {
        log.info("[DataPlatform] 주문 데이터 전송 - userId={}, productId={}, amount={}",
                userId, productId, amount);
    }
}
