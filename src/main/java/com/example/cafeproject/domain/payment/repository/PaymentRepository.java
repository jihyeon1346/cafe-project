package com.example.cafeproject.domain.payment.repository;

import com.example.cafeproject.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
