package com.example.demo.Service;

import com.example.demo.Entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CoinPriceRepository coinPriceRepository;
    private final FavoriteRepository favoriteRepository;

    public User findOrCreateUser(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseGet(() -> userRepository.save(new User(phoneNumber)));
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void addCoinPrice(String phoneNumber, String coinName, double price) {
        User user = findByPhoneNumber(phoneNumber);
        coinPriceRepository.save(new CoinPrice(null, coinName, price, user));
    }

    public void addFavorite(String phoneNumber, String coinName) {
        User user = findByPhoneNumber(phoneNumber);
        favoriteRepository.save(new Favorite(null, coinName, user));
    }
}