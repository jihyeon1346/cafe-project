package com.example.cafeproject.domain.point.service;

import com.example.cafeproject.domain.point.dto.ChargePointRequest;
import com.example.cafeproject.domain.point.dto.ChargePointResponse;
import com.example.cafeproject.domain.point.entity.Point;
import com.example.cafeproject.domain.point.repository.PointRepository;
import com.example.cafeproject.domain.point_transaction.entity.PointTransaction;
import com.example.cafeproject.domain.point_transaction.repository.PointTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointChargeService {

    private final PointRepository pointRepository;
    private final PointTransactionRepository pointTransactionRepository;

    @Transactional
    public ChargePointResponse charge(ChargePointRequest request) {
        Point point = pointRepository
                .findByUserId(request.getUserId())
                .orElseGet(() -> pointRepository.save(new Point(request.getUserId())));

        point.charge(request.getAmount());

        PointTransaction transaction = PointTransaction.charge(
                request.getAmount(),
                point.getBalance(),
                request.getUserId()
        );
        pointTransactionRepository.save(transaction);

        return new ChargePointResponse(
                request.getUserId(),
                request.getAmount(),
                point.getBalance(),
                point.getUpdatedAt()
        );
    }
}
