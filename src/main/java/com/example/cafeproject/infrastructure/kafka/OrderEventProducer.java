package com.example.cafeproject.infrastructure.kafka;

import com.example.cafeproject.common.model.kafka.event.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.example.cafeproject.common.model.kafka.topic.KafkaTopics.TOPIC_ORDER_COMPLETED;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCompletedEvent> orderCompletedEventKafkaTemplate;

    public void send(OrderCompletedEvent event) {
        orderCompletedEventKafkaTemplate.send(TOPIC_ORDER_COMPLETED, event);
    }
}
