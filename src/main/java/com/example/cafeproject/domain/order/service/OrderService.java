package com.example.cafeproject.domain.order.service;

import com.example.cafeproject.common.annotation.DistributedLock;
import com.example.cafeproject.common.exception.UserNotFoundException;
import com.example.cafeproject.domain.order.dto.CreateOrderRequest;
import com.example.cafeproject.domain.order.dto.CreateOrderResponse;
import com.example.cafeproject.domain.order_item.service.OrderItemService;
import com.example.cafeproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderItemService orderItemService;
    private final UserRepository userRepository;

    @DistributedLock(key = "'order:lock:' + #request.userId()")
    public CreateOrderResponse order(CreateOrderRequest request) {
        userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저 입니다."));

        return orderItemService.order(request);
    }
}
