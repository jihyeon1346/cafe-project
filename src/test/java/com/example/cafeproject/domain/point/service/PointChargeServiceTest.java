package com.example.cafeproject.domain.point.service;

import com.example.cafeproject.domain.point.dto.ChargePointRequest;
import com.example.cafeproject.domain.point.dto.ChargePointResponse;
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
class PointChargeServiceTest {

    @InjectMocks
    private PointChargeService pointChargeService;

    @Mock
    private PointRepository pointRepository;

    @Mock
    private PointTransactionRepository pointTransactionRepository;

    private ChargePointRequest makeRequest(Long userId, String amount) {
        try {
            ChargePointRequest request = new ChargePointRequest();
            var userIdField = ChargePointRequest.class.getDeclaredField("userId");
            var amountField = ChargePointRequest.class.getDeclaredField("amount");
            userIdField.setAccessible(true);
            amountField.setAccessible(true);
            userIdField.set(request, userId);
            amountField.set(request, new BigDecimal(amount));
            return request;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("기존 포인트가 있으면 잔액에 충전 금액이 누적된다")
    void charge_existing_point() {
        Long userId = 1L;
        Point existingPoint = new Point(userId);
        existingPoint.charge(new BigDecimal("10000")); // 기존 잔액 10000

        given(pointRepository.findByUserId(userId)).willReturn(Optional.of(existingPoint));
        given(pointTransactionRepository.save(any())).willReturn(null);

        ChargePointRequest request = makeRequest(userId, "5000");
        ChargePointResponse response = pointChargeService.charge(request);

        assertThat(response.getBalance()).isEqualByComparingTo("15000");
        assertThat(response.getChargedAmount()).isEqualByComparingTo("5000");
        verify(pointTransactionRepository).save(any(PointTransaction.class));
    }

    @Test
    @DisplayName("포인트가 없으면 새로 생성하고 충전한다")
    void charge_new_point() {
        Long userId = 2L;
        Point newPoint = new Point(userId);

        given(pointRepository.findByUserId(userId)).willReturn(Optional.empty());
        given(pointRepository.save(any(Point.class))).willReturn(newPoint);
        given(pointTransactionRepository.save(any())).willReturn(null);

        ChargePointRequest request = makeRequest(userId, "10000");
        ChargePointResponse response = pointChargeService.charge(request);

        assertThat(response.getBalance()).isEqualByComparingTo("10000");
        verify(pointRepository).save(any(Point.class));
    }

    @Test
    @DisplayName("충전 후 포인트 이력이 저장된다")
    void charge_saves_transaction() {
        Long userId = 1L;
        Point point = new Point(userId);

        given(pointRepository.findByUserId(userId)).willReturn(Optional.of(point));
        given(pointTransactionRepository.save(any())).willReturn(null);

        pointChargeService.charge(makeRequest(userId, "10000"));

        verify(pointTransactionRepository).save(any(PointTransaction.class));
    }
}