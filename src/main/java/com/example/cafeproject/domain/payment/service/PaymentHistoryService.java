package com.example.cafeproject.domain.payment.service;

import com.example.cafeproject.common.model.kafka.event.OrderCompletedEvent;
import com.example.cafeproject.domain.payment.entity.PaymentHistory;
import com.example.cafeproject.domain.payment.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentHistoryService {

    private final PaymentHistoryRepository paymentHistoryRepository;

    @Transactional
    public void savePaymentHistory(OrderCompletedEvent event) {
        PaymentHistory paymentHistory = PaymentHistory.from(event);
        paymentHistoryRepository.save(paymentHistory);

        log.info("[DataPlatform] 결제 기록 저장 완료 - orderId={}, userId={}, productId={}",
                event.getOrderId(), event.getUserId(), event.getProductId());
    }
}