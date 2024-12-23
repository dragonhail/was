package com.example.demo.Controller;

import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    HttpSession httpSession;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> viewDashboard(HttpSession session) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        User user = userService.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(user.getCoinPrices());
    }

    @PostMapping("/add-coin")
    public String addCoin(HttpSession session, @RequestParam String coinName, @RequestParam double price) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return "redirect:/login";
        }
        userService.addCoinPrice(phoneNumber, coinName, price);
        return "redirect:/dashboard";
    }

    @PostMapping("/logout")
    public RedirectView logout() {
        httpSession.invalidate(); // 세션 무효화
        return new RedirectView("/login"); // 로그아웃 후 로그인 페이지로 리다이렉트
    }

}
