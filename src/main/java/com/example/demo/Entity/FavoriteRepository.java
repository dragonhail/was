package com.example.demo.Entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, String> {
    // 특정 사용자 ID로 Favorite 목록 조회
    List<Favorite> findFavoriteByPhoneNumber(String phoneNumber);
}