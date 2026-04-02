package com.example.cafeproject.domain.point.service;

import com.example.cafeproject.common.annotation.DistributedLock;
import com.example.cafeproject.common.exception.InvalidAmountException;
import com.example.cafeproject.common.exception.UserNotFoundException;
import com.example.cafeproject.domain.point.dto.ChargePointRequest;
import com.example.cafeproject.domain.point.dto.ChargePointResponse;
import com.example.cafeproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointChargeService pointChargeService;
    private final UserRepository userRepository;

    @DistributedLock(key = "'point:lock:' + #request.getUserId()")
    public ChargePointResponse charge(ChargePointRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("충전 금액은 1원 이상이어야 합니다.");
        }

        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저 입니다."));

        return pointChargeService.charge(request);
    }
}
