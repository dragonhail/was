package com.example.demo.Controller;

import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    KafkaTemplate<String, String> kafkaTemplate;

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
    public RedirectView addCoin(HttpSession session, @RequestParam String coinName, @RequestParam double price) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return new RedirectView("/login");
        }
        // RDBMS에 사용자 코인 정보 추가
        userService.addCoinPrice(phoneNumber, coinName, price);

        // 카프카 메시지 생성
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> matchMessage = new HashMap<>();
        matchMessage.put("phoneNumber", phoneNumber);
        matchMessage.put("coinName", coinName);
        matchMessage.put("price", price);

        try {
            // JSON으로 변환 후 메시지 전송
            String message = objectMapper.writeValueAsString(matchMessage);
            kafkaTemplate.send("price-match", message);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 에러 로그 출력
            // 필요 시, 에러 상황에 대한 추가 처리
        }

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