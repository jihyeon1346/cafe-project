package com.example.cafeproject.domain.user.repository;

import com.example.cafeproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
