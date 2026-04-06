package com.example.cafeproject.domain.order.controller;

import com.example.cafeproject.common.dto.ApiResponse;
import com.example.cafeproject.domain.order.dto.CreateOrderRequest;
import com.example.cafeproject.domain.order.dto.CreateOrderResponse;
import com.example.cafeproject.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateOrderResponse>> order(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        CreateOrderResponse response = orderService.order(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(HttpStatus.CREATED, "주문 성공", response)
        );
    }
}
