package com.example.cafeproject.infrastructure.kafka;

import com.example.cafeproject.common.model.kafka.event.OrderCompletedEvent;
import com.example.cafeproject.domain.payment.service.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.cafeproject.common.model.kafka.topic.KafkaTopics.TOPIC_ORDER_COMPLETED;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentHistoryListener {

    private final PaymentHistoryService paymentHistoryService;

    @KafkaListener(
            topics = TOPIC_ORDER_COMPLETED,
            groupId = "payment-history-group",
            containerFactory = "paymentHistoryKafkaListenerContainerFactory"
    )
    public void consume(OrderCompletedEvent event) {
        log.info("[Consumer-History] 결제 완료 이벤트 수신 - orderId={}, userId={}",
                event.getOrderId(), event.getUserId());

        paymentHistoryService.savePaymentHistory(event);
    }
}