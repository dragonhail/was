package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    // 로그인 확인
    @GetMapping
    public ResponseEntity<String> getDashboard(HttpSession session) {
        // 등록된 전화번호가 없을 때
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(403).body("Forbidden: Login required");
        }

        // 전화번호가 있을 때
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(user -> ResponseEntity.ok("Welcome to your dashboard, " + user.getPhoneNumber()))
                .orElse(ResponseEntity.status(404).body("User not found"));
    }

    // 코인 가격 지정
    @PostMapping("/set-coin")
    public ResponseEntity<String> setCoinPreference(HttpSession session, @RequestParam String coinPreferences) {
        // 로그인 확인
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(403).body("Forbidden: Login required");
        }

        // 로그인 가능 시 코인 가격 저장
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setCoinPreferences(coinPreferences);
            userRepository.save(user);
            return ResponseEntity.ok("Preferences saved.");
        }

        return ResponseEntity.status(404).body("User not found");
    }
}
