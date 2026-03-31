package com.example.cafeproject.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${redis.stand-alone.host}")
    private String host;

    @Value("${redis.stand-alone.port}")
    private int port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setConnectionMinimumIdleSize(5)   // 최소 유휴 연결 수
                .setConnectionPoolSize(10)          // 최대 커넥션 풀 크기
                .setIdleConnectionTimeout(10000)   // 유휴 연결 타임아웃 (ms)
                .setConnectTimeout(10000)          // 연결 타임아웃 (ms)
                .setTimeout(3000)                  // 명령 실행 타임아웃 (ms)
                .setRetryAttempts(3)               // 재시도 횟수
                .setRetryInterval(1500);           // 재시도 간격 (ms)
        return Redisson.create(config);
    }
}
