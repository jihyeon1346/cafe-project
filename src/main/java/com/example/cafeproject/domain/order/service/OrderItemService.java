package com.example.cafeproject.domain.order.service;

import com.example.cafeproject.common.exception.InsufficientPointException;
import com.example.cafeproject.common.exception.ProductNotFoundException;
import com.example.cafeproject.common.exception.ProductNotOnSaleException;
import com.example.cafeproject.domain.order.dto.CreateOrderRequest;
import com.example.cafeproject.domain.order.dto.CreateOrderResponse;
import com.example.cafeproject.domain.order.dto.OrderItemRequest;
import com.example.cafeproject.domain.order.entity.Order;
import com.example.cafeproject.domain.order.entity.OrderItem;
import com.example.cafeproject.domain.order.repository.OrderItemRepository;
import com.example.cafeproject.domain.order.repository.OrderRepository;
import com.example.cafeproject.domain.payment.entity.Payment;
import com.example.cafeproject.domain.payment.repository.PaymentRepository;
import com.example.cafeproject.domain.point.entity.Point;
import com.example.cafeproject.domain.point.repository.PointRepository;
import com.example.cafeproject.domain.point_transaction.entity.PointTransaction;
import com.example.cafeproject.domain.point_transaction.repository.PointTransactionRepository;
import com.example.cafeproject.domain.product.entity.Product;
import com.example.cafeproject.domain.product.repository.ProductRepository;
import com.example.cafeproject.infrastructure.platform.DataPlatformClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final PointRepository pointRepository;
    private final OrderRepository orderRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final PaymentRepository paymentRepository;
    private final DataPlatformClient dataplatformClient;

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

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalStateException(
                        String.format("재고가 부족합니다. 메뉴: %s, 요청 수량: %d, 재고: %d",
                                product.getName(), itemRequest.getQuantity(), product.getQuantity())
                );
            }

            totalAmount = totalAmount.add(
                    product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()))
            );
            products.add(product);
        }

        // 포인트 잔액 확인
        Point point = pointRepository.findByUserId(request.getUserId())
                .orElseGet(() -> pointRepository.save(new Point(request.getUserId())));

        if (point.getBalance().compareTo(totalAmount) < 0) {
            throw new InsufficientPointException("포인트가 부족합니다.");
        }

        // 주문 생성
        Order savedOrder = new Order(request.getUserId(), totalAmount);
        orderRepository.save(savedOrder);

        // 재고 차감
        List<String> productNames = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            Long quantity = request.getItems().get(i).getQuantity();

            product.deductQuantity(quantity);

            orderItemRepository.save(
                    OrderItem.create(product.getId(), savedOrder.getId(), quantity, product.getPrice())
            );

            // 데이터 플랫폼 전송
            dataplatformClient.send(
                    request.getUserId(),
                    product.getId(),
                    product.getPrice().multiply(BigDecimal.valueOf(quantity))
            );

            productNames.add(product.getName());
        }

        // 포인트 차감
        point.use(totalAmount);
        pointTransactionRepository.save(
                PointTransaction.use(totalAmount, point.getBalance(), request.getUserId(), savedOrder.getId())
        );

        // 결제 완료
        paymentRepository.save(Payment.success(totalAmount, savedOrder.getId()));

        return new CreateOrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderNum(),
                productNames,
                totalAmount,
                point.getBalance(),
                savedOrder.getCreatedAt()
        );
    }
}