package com.example.demo.Entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CoinPriceRepository extends JpaRepository<CoinPrice, String> {
    public List<CoinPrice> findByUserId(String userId);
}
