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
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam String phoneNumber) {
        userService.findOrCreateUser(phoneNumber);
        authService.sendVerificationCode(phoneNumber);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/verify");
        return redirectView;
    }

    @GetMapping("verify")
    public String verify() {
        return "verify";
    }

    @PostMapping("/verify")
    public RedirectView verify(@RequestParam String phoneNumber, @RequestParam String code) {
        RedirectView redirectView = new RedirectView();
        if (authService.verifyCode(phoneNumber, code)) {
            httpSession.setAttribute("phoneNumber", phoneNumber);
            redirectView.setUrl("/dashboard");
        } else {
            redirectView.setUrl("/login?error=invalid_code");
        }
        return redirectView;
    }
}
