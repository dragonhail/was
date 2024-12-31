package com.example.demo.Controller;

import com.example.demo.Service.AuthService;
import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public ResponseEntity<?> home() {
        if (httpSession.getAttribute("user") != null) {
            return ResponseEntity.ok().body("Already Logged In");
        }
        return ResponseEntity.ok().body("You are logged in");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String phoneNumber) {
        userService.findOrCreateUser(phoneNumber);
        boolean isCodeSent = authService.sendVerificationCode(phoneNumber);
        if (isCodeSent) {
            return ResponseEntity.ok()
                    .body("Verification code sent successfully. Please check your phone.");
        } else {
            // 인증 코드 전송에 실패한 경우 오류 메시지 반환
            return ResponseEntity.status(400)
                    .body("Failed to send verification code. Please try again.");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String code) {
        RedirectView redirectView = new RedirectView();
        String phoneNumber = authService.verifyCodeByCodeOnlyEfficient(code);

        if (authService.verifyCode(phoneNumber, code)) {
            httpSession.setAttribute("phoneNumber", phoneNumber);
            return ResponseEntity.ok()
                    .body("Verified");
        } else {
            return ResponseEntity.status(400)
                    .body("Failed to verify code. Please try again.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        if (httpSession != null) {
            httpSession.invalidate(); // 세션 무효화
        }
        return ResponseEntity.ok().body("Logged out successfully.");
    }

}
