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
                .findByUserId(request.userId())
                .orElseGet(() -> pointRepository.save(new Point(request.userId())));

        point.charge(request.amount());

        PointTransaction transaction = PointTransaction.charge(
                request.amount(),
                point.getBalance(),
                request.userId()
        );
        pointTransactionRepository.save(transaction);

        return new ChargePointResponse(
                request.userId(),
                request.amount(),
                point.getBalance(),
                point.getUpdatedAt()
        );
    }
}
