package com.example.cafeproject.infrastructure.kafka;

import com.example.cafeproject.common.model.kafka.event.OrderCompletedEvent;
import com.example.cafeproject.infrastructure.redis.PopularMenuRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.cafeproject.common.model.kafka.topic.KafkaTopics.TOPIC_ORDER_COMPLETED;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductRankingListener {

    private final PopularMenuRedisService popularMenuRedisService;

    @KafkaListener(
            topics = TOPIC_ORDER_COMPLETED,
            groupId = "product-ranking-group",
            containerFactory = "productRankingKafkaListenerContainerFactory"
    )
    public void consume(OrderCompletedEvent event) {
        LocalDateTime paidAt = LocalDateTime.parse(event.getPaidAt());
        LocalDate currentDate = paidAt.toLocalDate();

        popularMenuRedisService.incrementScore(event.getProductId(), event.getQuantity(), currentDate);

        log.info("[Consumer1] 인기 메뉴 랭킹 업데이트 - productId: {}, quantity: {}, date={}",
                event.getProductId(), event.getQuantity(), currentDate);
    }
}
