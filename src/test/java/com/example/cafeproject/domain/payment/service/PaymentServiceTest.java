package com.example.cafeproject.domain.payment.service;

import com.example.cafeproject.domain.payment.repository.PaymentRepository;
import com.example.cafeproject.domain.point.entity.Point;
import com.example.cafeproject.domain.point.repository.PointRepository;
import com.example.cafeproject.domain.point_transaction.entity.PointTransaction;
import com.example.cafeproject.domain.point_transaction.repository.PointTransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock private PointRepository pointRepository;
    @Mock private PointTransactionRepository pointTransactionRepository;
    @Mock private PaymentRepository paymentRepository;

    @Test
    @DisplayName("결제 성공 시 포인트가 차감되고 잔액이 반환된다")
    void pay_success_deducts_point() {
        Point point = new Point(1L);
        point.charge(new BigDecimal("50000"));

        given(pointRepository.findByUserId(1L)).willReturn(Optional.of(point));
        given(paymentRepository.save(any())).willAnswer(inv -> inv.getArgument(0));
        given(pointTransactionRepository.save(any())).willReturn(null);

        BigDecimal remaining = paymentService.pay(1L, 100L, new BigDecimal("4500"));

        assertThat(remaining).isEqualByComparingTo("45500");
    }

    @Test
    @DisplayName("결제 성공 시 포인트 이력이 저장된다")
    void pay_saves_point_transaction() {
        Point point = new Point(1L);
        point.charge(new BigDecimal("50000"));

        given(pointRepository.findByUserId(1L)).willReturn(Optional.of(point));
        given(paymentRepository.save(any())).willAnswer(inv -> inv.getArgument(0));
        given(pointTransactionRepository.save(any())).willReturn(null);

        paymentService.pay(1L, 100L, new BigDecimal("9000"));

        verify(pointTransactionRepository).save(any(PointTransaction.class));
    }
}