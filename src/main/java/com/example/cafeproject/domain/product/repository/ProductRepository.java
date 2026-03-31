package com.example.cafeproject.domain.product.repository;

import com.example.cafeproject.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
