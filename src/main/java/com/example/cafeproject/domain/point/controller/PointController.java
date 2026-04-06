package com.example.cafeproject.domain.point.controller;

import com.example.cafeproject.common.dto.ApiResponse;
import com.example.cafeproject.domain.point.dto.ChargePointRequest;
import com.example.cafeproject.domain.point.dto.ChargePointResponse;
import com.example.cafeproject.domain.point.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/charge")
    public ResponseEntity<ApiResponse<ChargePointResponse>> charge(
            @Valid @RequestBody ChargePointRequest request
    ) {
        ChargePointResponse response = pointService.charge(request);
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "포인트 충전 성공", response)
        );
    }
}
