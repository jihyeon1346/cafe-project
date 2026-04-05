package com.example.cafeproject.domain.payment.service;

import com.example.cafeproject.common.exception.InsufficientPointException;
import com.example.cafeproject.domain.payment.entity.Payment;
import com.example.cafeproject.domain.payment.repository.PaymentRepository;
import com.example.cafeproject.domain.point.entity.Point;
import com.example.cafeproject.domain.point.repository.PointRepository;
import com.example.cafeproject.domain.point_transaction.entity.PointTransaction;
import com.example.cafeproject.domain.point_transaction.repository.PointTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PointRepository pointRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public BigDecimal pay(Long userId, Long orderId, BigDecimal amount) {

        // 포인트 조회 및 잔액 검증
        Point point = pointRepository.findByUserId(userId)
                .orElseGet(() -> pointRepository.save(new Point(userId)));

        if (point.getBalance().compareTo(amount) < 0) {
            throw new InsufficientPointException("포인트가 부족합니다.");
        }

        // 포인트 차감
        point.use(amount);

        // 포인트 이력 저장
        pointTransactionRepository.save(
                PointTransaction.use(amount, point.getBalance(), userId, orderId)
        );

        // 결제 성공 기록
        paymentRepository.save(Payment.success(amount, orderId));

        return point.getBalance();
    }
}