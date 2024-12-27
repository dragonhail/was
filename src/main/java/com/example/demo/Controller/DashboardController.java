package com.example.demo.Controller;

import com.example.demo.Entity.CoinPrice;
import com.example.demo.Entity.CoinPriceDTO;
import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    HttpSession httpSession;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> viewDashboard(HttpSession session) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 하세요");
        }
        User user = userService.findByPhoneNumber(phoneNumber);
        // Convert CoinPrice entities to DTOs using a for loop
        List<CoinPriceDTO> coinPriceDTOs = new ArrayList<>();
        for (CoinPrice coinPrice : user.getCoinPrices()) {
            coinPriceDTOs.add(new CoinPriceDTO(coinPrice));
        }
        return ResponseEntity.ok(coinPriceDTOs);
    }

    @PostMapping("/add-coin")
    public RedirectView addCoin(HttpSession session, @RequestParam String coinName, @RequestParam double price) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return new RedirectView("/login");
        }
        kafkaTemplate.send("price-watch", coinName + ":" + price);
        // RDBMS에 사용자 코인 정보 추가
        userService.addCoinPrice(phoneNumber, coinName, price);

        return new RedirectView("/dashboard");
    }

    @PostMapping("/logout")
    public RedirectView logout() {
        if (httpSession != null) {
            httpSession.invalidate(); // 세션 무효화
        }
        return new RedirectView("/login"); // 로그아웃 후 로그인 페이지로 리다이렉트
    }
}