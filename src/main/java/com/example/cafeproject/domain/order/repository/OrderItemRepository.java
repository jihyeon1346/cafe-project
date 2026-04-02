package com.example.cafeproject.domain.order.repository;

import com.example.cafeproject.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
}
