package com.example.demo.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void saveVerificationCode(String phoneNumber, String code) {
        redisTemplate.opsForValue().set(phoneNumber, code, 5, TimeUnit.MINUTES); // 5분 유효
    }

    public String getVerificationCode(String phoneNumber) {
        return redisTemplate.opsForValue().get(phoneNumber);
    }

    public void deleteVerificationCode(String phoneNumber) {
        redisTemplate.delete(phoneNumber);
    }
}
