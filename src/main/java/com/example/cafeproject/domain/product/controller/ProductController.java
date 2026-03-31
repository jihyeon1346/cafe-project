package com.example.cafeproject.domain.product.controller;

import com.example.cafeproject.common.dto.ApiResponse;
import com.example.cafeproject.domain.product.dto.GetProductResponse;
import com.example.cafeproject.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetProductResponse>>> getAllProducts() {
        return ResponseEntity.ok().body(
                ApiResponse.success(
                        HttpStatus.OK, "상품 목록 조회 성공", productService.getAllProducts()
                ));
    }
}
