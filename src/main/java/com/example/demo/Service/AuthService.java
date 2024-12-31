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

    public Boolean sendVerificationCode(String phoneNumber) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        redisTemplate.opsForValue().set(phoneNumber, code, 5, TimeUnit.MINUTES);
        redisTemplate.opsForHash().put("CodeToPhoneMap", code, phoneNumber); // 코드 -> 전화번호 매핑 추가
        redisTemplate.expire("CodeToPhoneMap", 5, TimeUnit.MINUTES); // 해시맵에도 동일한 TTL 설정
        // 메시지 전송 로직 추가 (예: Twilio)
        return Boolean.TRUE;
    }

    public boolean verifyCode(String phoneNumber, String code) {
        String savedCode = redisTemplate.opsForValue().get(phoneNumber);
        return code.equals(savedCode);
    }

    public String verifyCodeByCodeOnlyEfficient(String code) {
        return (String) redisTemplate.opsForHash().get("CodeToPhoneMap", code); // 코드로 전화번호 검색
    }
}
