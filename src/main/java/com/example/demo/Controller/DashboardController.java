package com.example.demo.Controller;

import com.example.demo.Entity.CoinPrice;
import com.example.demo.Entity.CoinPriceRepository;
import com.example.demo.Entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final CoinPriceRepository coinPriceRepository;

    @GetMapping
    public ResponseEntity<List<CoinPrice>> getCoinPrices(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(user.getCoinPrices());
    }

    @PostMapping
    public ResponseEntity<CoinPrice> addCoinPrice(HttpSession session,
                                                  @RequestParam String coinName,
                                                  @RequestParam double targetPrice) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(403).build();
        }
        CoinPrice coinPrice = new CoinPrice();
        coinPrice.setCoinName(coinName);
        coinPrice.setTargetPrice(targetPrice);
        coinPrice.setUser(user);
        return ResponseEntity.ok(coinPriceRepository.save(coinPrice));
    }
}
