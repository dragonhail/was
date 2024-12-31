package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DashboardController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    HttpSession httpSession;
    private final UserService userService;
    private final FavoriteRepository favoriteRepository;

    @GetMapping("/target-price")
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

    @PostMapping("/target-price")
    public ResponseEntity<?> addCoin(HttpSession session, @RequestParam String coinName, @RequestParam double price) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 하세요");
        }

        kafkaTemplate.send("price-watch", "add:"+coinName + ":" + price);
        // RDBMS에 사용자 코인 정보 추가
        userService.addCoinPrice(phoneNumber, coinName, price);

        return ResponseEntity.ok().body("가격 지정 되었습니다");
    }

    @GetMapping("favorites")
    public ResponseEntity<?> viewFavorite(HttpSession session) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 하세요");
        }

        // phoneNumber로 사용자 선호 코인 목록 조회
        List<Favorite> favorites = favoriteRepository.findFavoriteByPhoneNumber(phoneNumber);

        // Favorite을 FavoriteRequest로 변환
        List<FavoriteRequest> favoriteDTOs = new ArrayList<>();
        for (Favorite favorite : favorites) {
            favoriteDTOs.add(new FavoriteRequest(favorite));
        }
        return ResponseEntity.ok(favoriteDTOs);
    }

    @PostMapping("favorites")
    public ResponseEntity<?> addFavorite(HttpSession session, @RequestBody FavoriteRequest favoriteRequest) {
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        if (phoneNumber == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 하세요");
        }
        User user = userService.findByPhoneNumber(phoneNumber);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        // Favorite 객체 생성
        Favorite favorite = new Favorite();
        favorite.setUser(user); // 현재 사용자와 연관
        favorite.setCoinName(favoriteRequest.getCoinName()); // FavoriteRequest에서 코인 이름 설정

        // Favorite 저장
        favoriteRepository.save(favorite);
        return ResponseEntity.ok().body("Favorites Posted");
    }
}
