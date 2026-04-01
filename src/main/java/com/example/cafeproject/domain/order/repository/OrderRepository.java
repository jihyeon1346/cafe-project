package com.example.cafeproject.domain.order.repository;

import com.example.cafeproject.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
