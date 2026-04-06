package com.example.cafeproject.domain.point_transaction.repository;

import com.example.cafeproject.domain.point_transaction.entity.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
}
