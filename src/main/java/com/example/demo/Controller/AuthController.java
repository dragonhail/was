package com.example.demo.Controller;

import com.example.demo.Service.AuthService;
import com.example.demo.Entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestParam String phoneNumber) {
        String code = authService.sendVerificationCode(phoneNumber);
        return ResponseEntity.ok("Verification code sent: " + code);
    }

    @PostMapping("/verify")
    public ResponseEntity<User> verifyCode(@RequestParam String phoneNumber,
                                           @RequestParam String code,
                                           HttpSession session) {
        if (!authService.verifyCode(phoneNumber, code)) {
            return ResponseEntity.badRequest().body(null);
        }
        User user = authService.registerOrLogin(phoneNumber, session);
        return ResponseEntity.ok(user);
    }
}
