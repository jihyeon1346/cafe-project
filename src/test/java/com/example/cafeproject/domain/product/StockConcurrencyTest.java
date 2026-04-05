package com.example.cafeproject.domain.product;

import com.example.cafeproject.domain.product.consts.ProductStatus;
import com.example.cafeproject.domain.product.entity.Product;
import com.example.cafeproject.domain.product.repository.ProductRepository;
import com.example.cafeproject.domain.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockConcurrencyTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("동시에 100개 주문해도 재고가 정확히 차감된다")
    void concurrency_stock_deduction() throws InterruptedException {
        // given
        Product product = productRepository.save(
                new Product("아메리카노", new BigDecimal("4500"), 100L, ProductStatus.ON_SALE)
        );
        Long productId = product.getId();

        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    productService.deductStock(productId, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // then
        Product result = productRepository.findById(productId).get();
        assertThat(result.getQuantity()).isEqualTo(0L);
    }
}
