package com.example.demo.Service;

import com.example.demo.Entity.CoinPrice;
import com.example.demo.Entity.User;
import com.example.demo.Entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findOrCreateUser(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseGet(() -> userRepository.save(new User(null, phoneNumber, new ArrayList<>())));
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void addCoinPrice(String phoneNumber, String coinName, double price) {
        User user = findByPhoneNumber(phoneNumber);
        user.getCoinPrices().add(new CoinPrice(null, coinName, price, user));
        userRepository.save(user);
    }
}
