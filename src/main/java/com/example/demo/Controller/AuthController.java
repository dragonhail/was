package com.example.demo.Controller;

import com.example.demo.Service.AuthService;
import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public RedirectView home() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(httpSession.getAttribute("phoneNumber") != null ? "/dashboard" : "/login");
        return redirectView;
    }

    @GetMapping("/login")
    public String login() {
        return "로그인화면";
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam String phoneNumber) {
        userService.findOrCreateUser(phoneNumber);
        authService.sendVerificationCode(phoneNumber);
        return new RedirectView("/verify");
    }

    @GetMapping("verify")
    public String verify() {
        return "인증번호 입력화면";
    }

    @PostMapping("/verify")
    public RedirectView verify(@RequestParam String code) {
        RedirectView redirectView = new RedirectView();
        String phoneNumber = authService.verifyCodeByCodeOnlyEfficient(code);

        if (authService.verifyCode(phoneNumber, code)) {
            httpSession.setAttribute("phoneNumber", phoneNumber);
            redirectView.setUrl("/dashboard");
        } else {
            redirectView.setUrl("/login");
        }
        return redirectView;
    }
}
