package com.example.cafeproject.domain.order.service;

import com.example.cafeproject.common.exception.InsufficientPointException;
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
import com.example.cafeproject.domain.product.consts.ProductStatus;
import com.example.cafeproject.domain.product.entity.Product;
import com.example.cafeproject.domain.product.repository.ProductRepository;
import com.example.cafeproject.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @InjectMocks
    private OrderItemService orderItemService;

    @Mock private OrderItemRepository orderItemRepository;
    @Mock private ProductRepository productRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private PaymentService paymentService;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private ProductService productService;

    private Product americano;

    @BeforeEach
    void setUp() throws Exception {
        americano = new Product("아메리카노", new BigDecimal("4500"), 10L, ProductStatus.ON_SALE);
        setField(americano, "id", 1L);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private CreateOrderRequest makeRequest(Long userId, Long productId, Long quantity) {
        try {
            OrderItemRequest item = new OrderItemRequest();
            var pidField = OrderItemRequest.class.getDeclaredField("productId");
            var qtyField = OrderItemRequest.class.getDeclaredField("quantity");
            pidField.setAccessible(true);
            qtyField.setAccessible(true);
            pidField.set(item, productId);
            qtyField.set(item, quantity);

            CreateOrderRequest request = new CreateOrderRequest();
            var uidField = CreateOrderRequest.class.getDeclaredField("userId");
            var itemsField = CreateOrderRequest.class.getDeclaredField("items");
            uidField.setAccessible(true);
            itemsField.setAccessible(true);
            uidField.set(request, userId);
            itemsField.set(request, List.of(item));
            return request;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("정상 주문 시 총 금액이 올바르게 계산된다")
    void order_success_total_amount() {
        given(productRepository.findById(1L)).willReturn(Optional.of(americano));
        given(orderRepository.save(any(Order.class))).willAnswer(inv -> inv.getArgument(0));
        given(paymentService.pay(eq(1L), any(), eq(new BigDecimal("9000"))))
                .willReturn(new BigDecimal("41000"));
        given(orderItemRepository.save(any(OrderItem.class))).willAnswer(inv -> inv.getArgument(0));

        CreateOrderResponse response = orderItemService.order(makeRequest(1L, 1L, 2L));

        assertThat(response.getTotalAmount()).isEqualByComparingTo("9000");
        assertThat(response.getRemainingBalance()).isEqualByComparingTo("41000");
    }

    @Test
    @DisplayName("정상 주문 시 PaymentService.pay()가 호출된다")
    void order_calls_payment_service() {
        given(productRepository.findById(1L)).willReturn(Optional.of(americano));
        given(orderRepository.save(any())).willAnswer(inv -> inv.getArgument(0));
        given(paymentService.pay(any(), any(), any())).willReturn(new BigDecimal("45500"));
        given(orderItemRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        orderItemService.order(makeRequest(1L, 1L, 1L));

        verify(paymentService).pay(eq(1L), any(), eq(new BigDecimal("4500")));
    }

    @Test
    @DisplayName("정상 주문 시 커밋 후 이벤트가 발행된다 (Kafka + DataPlatform)")
    void order_publishes_after_commit_event() {
        given(productRepository.findById(1L)).willReturn(Optional.of(americano));
        given(orderRepository.save(any())).willAnswer(inv -> inv.getArgument(0));
        given(paymentService.pay(any(), any(), any())).willReturn(new BigDecimal("45500"));
        given(orderItemRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        orderItemService.order(makeRequest(1L, 1L, 1L));

        verify(eventPublisher).publishEvent(any(OrderItemCompletedEvent.class));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 주문 시 예외가 발생한다")
    void order_product_not_found() {
        given(productRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderItemService.order(makeRequest(1L, 999L, 1L)))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("판매 중이 아닌 메뉴 주문 시 예외가 발생한다")
    void order_product_not_on_sale() {
        Product soldOut = new Product("딸기 스무디", new BigDecimal("6500"), 0L, ProductStatus.SOLD_OUT);
        given(productRepository.findById(2L)).willReturn(Optional.of(soldOut));

        assertThatThrownBy(() -> orderItemService.order(makeRequest(1L, 2L, 1L)))
                .isInstanceOf(ProductNotOnSaleException.class);
    }

    @Test
    @DisplayName("결제 실패 시 재고 차감이 실행되지 않는다")
    void order_payment_fail_no_stock_deduction() {
        given(productRepository.findById(1L)).willReturn(Optional.of(americano));
        given(orderRepository.save(any())).willAnswer(inv -> inv.getArgument(0));
        willThrow(new InsufficientPointException("포인트가 부족합니다."))
                .given(paymentService).pay(any(), any(), any());

        assertThatThrownBy(() -> orderItemService.order(makeRequest(1L, 1L, 1L)))
                .isInstanceOf(InsufficientPointException.class);

        verify(productService, never()).deductStock(any(), any());
    }

    @Test
    @DisplayName("주문 성공 시 재고 차감이 호출된다")
    void order_deducts_stock() {
        given(productRepository.findById(1L)).willReturn(Optional.of(americano));
        given(orderRepository.save(any())).willAnswer(inv -> inv.getArgument(0));
        given(paymentService.pay(any(), any(), any())).willReturn(BigDecimal.ZERO);
        given(orderItemRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        orderItemService.order(makeRequest(1L, 1L, 3L));

        verify(productService).deductStock(eq(1L), eq(3L));
    }
}