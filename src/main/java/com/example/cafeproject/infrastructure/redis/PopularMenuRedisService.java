package com.example.cafeproject.infrastructure.redis;

import com.example.cafeproject.domain.product.dto.PopularMenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PopularMenuRedisService {

    private static final String KEY_PREFIX = "popular:menu:";
    private static final String UNION_KEY_PREFIX = "popular:menu:union:";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int TTL_DAYS = 8;

    private final StringRedisTemplate redisTemplate;

    public void incrementScore(Long productId, Long quantity, LocalDate date) {
        String key = buildDailyKey(date);
        redisTemplate.opsForZSet().incrementScore(key, String.valueOf(productId), quantity.doubleValue());
        redisTemplate.expire(key, Duration.ofDays(TTL_DAYS));
    }

    public List<PopularMenuDto> getTopProducts(int limit) {
        List<String> dailyKeys = getLast7DaysKeys();
        if (dailyKeys.isEmpty()) {
            return List.of();
        }

        String queryKey;

        if (dailyKeys.size() == 1) {
            queryKey = dailyKeys.get(0);
        } else {
            // 키가 2개 이상이면 union 후 조회
            queryKey = UNION_KEY_PREFIX + LocalDate.now().format(DATE_FORMATTER);
            redisTemplate.opsForZSet().unionAndStore(
                    dailyKeys.get(0),
                    dailyKeys.subList(1, dailyKeys.size()),
                    queryKey
            );
            redisTemplate.expire(queryKey, Duration.ofMinutes(1));
        }

        Set<ZSetOperations.TypedTuple<String>> topSet =
                redisTemplate.opsForZSet().reverseRangeWithScores(queryKey, 0, limit - 1);

        if (topSet == null || topSet.isEmpty()) {
            return List.of();
        }

        return topSet.stream()
                .map(tuple -> new PopularMenuDto(
                        Long.parseLong(tuple.getValue()),
                        tuple.getScore().longValue()
                ))
                .toList();
    }

    private String buildDailyKey(LocalDate date) {
        return KEY_PREFIX + date.format(DATE_FORMATTER);
    }

    private List<String> getLast7DaysKeys() {
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            String key = buildDailyKey(LocalDate.now().minusDays(i));
            if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                keys.add(key);
            }
        }
        return keys;
    }
}
