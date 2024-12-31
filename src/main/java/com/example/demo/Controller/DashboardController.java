package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {
    HttpSession httpSession;
    private final UserService userService;

    @GetMapping("/target-price")
    public ResponseEntity<?> viewDashboard(HttpSession session) {
        if (session==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 하세요");
        }
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 하세요");
        }
        User user = userService.findByPhoneNumber(phoneNumber);
        List<CoinPriceDTO> coinPriceDTOS = new ArrayList<>();

        List<CoinPrice> coinPrices = user.getCoinPrices();
        for (CoinPrice coinPrice : coinPrices) {
            coinPriceDTOS.add(new CoinPriceDTO(coinPrice));
        }
        return ResponseEntity.ok(coinPriceDTOS);
    }

    @PostMapping("/target-price")
    public ResponseEntity<?> addCoin(HttpSession session, String coinName, double price) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 하세요");
        }
        userService.addCoinPrice(phoneNumber, coinName, price);
        return ResponseEntity.status(HttpStatus.CREATED).body("코인 가격 지정 성공");
    }

    @GetMapping("favorites")
    public ResponseEntity<?> viewFavorite(HttpSession session) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 하세요");
        }
        User user = userService.findByPhoneNumber(phoneNumber);
        List<FavoriteRequest> favoriteRequests = new ArrayList<>();

        // 사용자에 해당하는 favorite 리스트를 가져와서 FavoriteRequest로 변환
        List<Favorite> favorites = user.getFavorites();
        for (Favorite favorite : favorites) {
            favoriteRequests.add(new FavoriteRequest(favorite));
        }
        return ResponseEntity.ok(favoriteRequests);
    }

    @PostMapping("favorites")
    public ResponseEntity<?> addFavorite(HttpSession session, @RequestParam String coinName) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 하세요");
        }
        User user = userService.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
        userService.addFavorite(phoneNumber, coinName);
        return ResponseEntity.ok("생성되었습니다");
    }
}
