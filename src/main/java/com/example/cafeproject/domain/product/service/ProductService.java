package com.example.cafeproject.domain.product.service;

import com.example.cafeproject.domain.product.dto.GetProductResponse;
import com.example.cafeproject.domain.product.dto.PopularMenuDto;
import com.example.cafeproject.domain.product.dto.PopularProductResponse;
import com.example.cafeproject.domain.product.entity.Product;
import com.example.cafeproject.domain.product.repository.ProductRepository;
import com.example.cafeproject.infrastructure.redis.PopularMenuRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private static final int POPULAR_MENU_LIMIT = 3;

    private final ProductRepository productRepository;
    private final PopularMenuRedisService popularMenuRedisService;

    @Transactional(readOnly = true)
    public List<GetProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(p -> new GetProductResponse(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getStatus(),
                        p.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PopularProductResponse> getPopularProducts() {
        List<PopularMenuDto> topProducts = popularMenuRedisService.getTopProducts(POPULAR_MENU_LIMIT);

        if (topProducts.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = topProducts.stream()
                .map(PopularMenuDto::getProductId)
                .toList();

        Map<Long, Product> productMap = productRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<PopularProductResponse> result = new ArrayList<>();
        for (int i = 0; i < topProducts.size(); i++) {
            PopularMenuDto menu = topProducts.get(i);
            Product product = productMap.get(menu.getProductId());

            if (product == null) {
                log.warn("DB에 없는 상품 id: {}", menu.getProductId());
                continue;
            }

            result.add(new PopularProductResponse(
                    i + 1, product.getId(),
                    product.getName(),
                    product.getPrice(),
                    menu.getOrderCount(),
                    product.getCreatedAt()));
        }
        return result;
    }
}
