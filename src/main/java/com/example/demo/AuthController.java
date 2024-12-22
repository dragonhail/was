package com.example.demo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;
import java.util.Random;
// 인증문자 발송 및 인증 및 로그아웃
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwilioService twilioService;

    private final Random random = new Random();

    // 인증문자 발송
    @PostMapping("/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody @NotBlank String phoneNumber) {
        String code = String.valueOf(100000 + random.nextInt(900000)); // 6자리 코드 생성
        twilioService.sendVerificationCode(phoneNumber, code);

        // User 있으면 인증문자 발송하고 없으면 User 생성 후 인증문자 발송
        userRepository.findByPhoneNumber(phoneNumber)
                .ifPresentOrElse(
                        user -> {
                            user.setVerificationCode(code);
                            userRepository.save(user);
                        },
                        () -> userRepository.save(User.builder()
                                .phoneNumber(phoneNumber)
                                .verificationCode(code)
                                .loggedIn(false)
                                .build())
                );

        return ResponseEntity.ok("200");
    }

    // react로 인증문자 발송 시 연결
    @PostMapping("/verify-code")
    public RedirectView verifyCode(@RequestParam String phoneNumber, @RequestParam String code, HttpSession session) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);

        if (userOptional.isPresent() && userOptional.get().getVerificationCode().equals(code)) {
            User user = userOptional.get();
            user.setLoggedIn(true);
            userRepository.save(user);

            // 세션에 사용자 정보 저장
            session.setAttribute("userId", user.getId());
            session.setAttribute("phoneNumber", user.getPhoneNumber());

            if (user.isLoggedIn()) {
                return new RedirectView("/dashboard");
            } else {
                return new RedirectView("/set-target");
            }
        }
        return new RedirectView("/auth/login");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Redis에서 세션 삭제
        return ResponseEntity.ok("Logged out successfully.");
    }

}
