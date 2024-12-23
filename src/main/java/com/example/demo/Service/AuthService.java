package com.example.demo.Service;

import com.example.demo.Entity.User;
import com.example.demo.Entity.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RedisService redisService;

    public String sendVerificationCode(String phoneNumber) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000); // 6자리 코드 생성
        redisService.saveVerificationCode(phoneNumber, code);
        // 실제로는 문자 메시지 API를 사용해 전송
        return code;
    }

    public boolean verifyCode(String phoneNumber, String code) {
        String savedCode = redisService.getVerificationCode(phoneNumber);
        if (code.equals(savedCode)) {
            redisService.deleteVerificationCode(phoneNumber);
            return true;
        }
        return false;
    }

    public User registerOrLogin(String phoneNumber, HttpSession session) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseGet(() -> {
            User newUser = new User();
            newUser.setPhoneNumber(phoneNumber);
            return userRepository.save(newUser);
        });

        session.setAttribute("user", user);
        return user;
    }
}
