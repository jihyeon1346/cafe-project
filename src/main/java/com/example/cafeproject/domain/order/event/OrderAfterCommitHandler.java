package com.example.cafeproject.domain.order.event;

import com.example.cafeproject.common.model.kafka.event.OrderCompletedEvent;
import com.example.cafeproject.infrastructure.kafka.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderAfterCommitHandler {

    private final OrderEventProducer orderEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderItemCompletedEvent event) {

        try {
            orderEventProducer.send(OrderCompletedEvent.builder()
                    .orderId(event.getOrderId())
                    .userId(event.getUserId())
                    .productId(event.getProductId())
                    .quantity(event.getQuantity())
                    .paymentAmount(event.getPaymentAmount())
                    .paidAt(event.getPaidAt())
                    .build()
            );
        } catch (Exception e) {
            log.error("[OrderAfterCommit] Kafka 이벤트 발행 실패 - orderId={}, productId={}",
                    event.getOrderId(), event.getProductId(), e);
        }
    }
}