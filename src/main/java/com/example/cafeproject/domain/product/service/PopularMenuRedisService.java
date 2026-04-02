package com.example.cafeproject.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PopularMenuRedisService {

    private static final String KEY_PREFIX = "popular:menu:";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int TTL_DAYS = 8;

    private final StringRedisTemplate redisTemplate;

    public void incrementScore(Long productId, Long quantity) {
        String todayKey = buildDailyKey(LocalDate.now());
        redisTemplate.opsForZSet().incrementScore(todayKey, String.valueOf(productId), quantity.doubleValue());
        redisTemplate.expire(todayKey, Duration.ofDays(TTL_DAYS));
    }

    private String buildDailyKey(LocalDate date) {
        return KEY_PREFIX + date.format(DATE_FORMATTER);
    }
}