package com.example.cafeproject.domain.product.service;

import com.example.cafeproject.domain.product.dto.GetProductResponse;
import com.example.cafeproject.domain.product.dto.PopularProductResponse;
import com.example.cafeproject.domain.product.entity.Product;
import com.example.cafeproject.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

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

    public List<PopularProductResponse> getPopularProducts() {
        return null;
    }
}
