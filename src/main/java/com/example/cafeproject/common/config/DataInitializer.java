package com.example.cafeproject.common.config;

import com.example.cafeproject.domain.point.entity.Point;
import com.example.cafeproject.domain.point.repository.PointRepository;
import com.example.cafeproject.domain.product.consts.ProductStatus;
import com.example.cafeproject.domain.product.entity.Product;
import com.example.cafeproject.domain.product.repository.ProductRepository;
import com.example.cafeproject.domain.user.entity.User;
import com.example.cafeproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    @Override
    public void run(ApplicationArguments args) {
        initProducts();
        initUsers();
    }

    private void initProducts() {
        if (productRepository.count() > 0) return;
        productRepository.saveAll(List.of(
                new Product("아메리카노",      new BigDecimal("4500"), 100L, ProductStatus.ON_SALE),
                new Product("카페라떼",        new BigDecimal("5000"),  80L, ProductStatus.ON_SALE),
                new Product("카푸치노",        new BigDecimal("5000"),  60L, ProductStatus.ON_SALE),
                new Product("바닐라 라떼",     new BigDecimal("5500"),  50L, ProductStatus.ON_SALE),
                new Product("카라멜 마키아토", new BigDecimal("5500"),  40L, ProductStatus.ON_SALE),
                new Product("녹차 라떼",       new BigDecimal("5500"),  30L, ProductStatus.ON_SALE),
                new Product("딸기 스무디",     new BigDecimal("6500"),   0L, ProductStatus.SOLD_OUT),
                new Product("자몽 에이드",     new BigDecimal("6000"),  20L, ProductStatus.ON_SALE),
                new Product("초코 프라푸치노", new BigDecimal("6500"),  10L, ProductStatus.ON_SALE),
                new Product("시즌 한정 피치티",new BigDecimal("6000"),   0L, ProductStatus.DISCONTINUED)
        ));
    }

    private void initUsers() {
        if (userRepository.count() > 0) return;

        List<User> users = userRepository.saveAll(List.of(
                new User("테스트유저1", "user1@test.com", "hashed_password_1", "서울시 강남구", "010-1111-1111"),
                new User("테스트유저2", "user2@test.com", "hashed_password_2", "서울시 마포구", "010-2222-2222")
        ));

        List<Point> points = users.stream()
                .map(u -> new Point(u.getId()))
                .toList();
        pointRepository.saveAll(points);
    }
}
