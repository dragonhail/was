package com.example.demo.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisTemplate<String, String> redisTemplate;

    public String sendVerificationCode(String phoneNumber) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        redisTemplate.opsForValue().set(code, phoneNumber, 5, TimeUnit.MINUTES);
        // 메시지 전송 로직 추가 (예: Twilio)
        return code;
    }

    public boolean verifyCode(String phoneNumber, String code) {
        String savedCode = redisTemplate.opsForValue().get(code);
        return code.equals(savedCode);

    }
}
