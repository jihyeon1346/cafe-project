package com.example.cafeproject.domain.order.service;

import com.example.cafeproject.common.exception.ProductNotFoundException;
import com.example.cafeproject.common.exception.ProductNotOnSaleException;
import com.example.cafeproject.domain.order.dto.CreateOrderRequest;
import com.example.cafeproject.domain.order.dto.CreateOrderResponse;
import com.example.cafeproject.domain.order.dto.OrderItemRequest;
import com.example.cafeproject.domain.order.entity.Order;
import com.example.cafeproject.domain.order.entity.OrderItem;
import com.example.cafeproject.domain.order.event.OrderItemCompletedEvent;
import com.example.cafeproject.domain.order.repository.OrderItemRepository;
import com.example.cafeproject.domain.order.repository.OrderRepository;
import com.example.cafeproject.domain.payment.service.PaymentService;
import com.example.cafeproject.domain.product.entity.Product;
import com.example.cafeproject.domain.product.repository.ProductRepository;
import com.example.cafeproject.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductService productService;

    @Transactional
    public CreateOrderResponse order(CreateOrderRequest request) {

        // 모든 메뉴 조회 및 검증
        List<Product> products = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 메뉴입니다."));

            if (!product.isOnSale()) {
                throw new ProductNotOnSaleException("주문할 수 없는 메뉴입니다. : " + product.getName());
            }

            totalAmount = totalAmount.add(
                    product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()))
            );
            products.add(product);
        }

        // 주문 생성
        Order savedOrder = new Order(request.getUserId(), totalAmount);
        orderRepository.save(savedOrder);

        // 결제
        BigDecimal remainingBalance = paymentService.pay(
                request.getUserId(), savedOrder.getId(), totalAmount
        );

        // 재고 차감 및 주문 상품 저장
        List<String> productNames = new ArrayList<>();
        String paidAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            Long quantity = request.getItems().get(i).getQuantity();

            productService.deductStock(product.getId(), quantity);

            orderItemRepository.save(
                    OrderItem.create(product.getId(), savedOrder.getId(), quantity, product.getPrice())
            );

            productNames.add(product.getName());

            eventPublisher.publishEvent(new OrderItemCompletedEvent(
                    savedOrder.getId(),
                    request.getUserId(),
                    product.getId(),
                    quantity,
                    product.getPrice().multiply(BigDecimal.valueOf(quantity)),
                    paidAt
            ));
        }

        return new CreateOrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderNum(),
                productNames,
                totalAmount,
                remainingBalance,
                savedOrder.getCreatedAt()
        );
    }
}