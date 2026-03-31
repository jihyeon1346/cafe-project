package com.example.cafeproject.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    String key();

    long waitTime() default 5;   // 락 획득 대기 시간

    long leaseTime() default 10; // 락 자동 해제 시간
}
