package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(unique = true, nullable = false)
    private String id;

    @Column(unique=true, nullable = false)
    private String phoneNumber;

    // 사용자 지정 코인 가격
    @JsonIgnore // User 객체의 coinPrices 직렬화 방지
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CoinPrice> coinPrices;

    // 사용자 선호 코인
    @JsonIgnore // User 객체의 favorites 직렬화 방지
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites;

    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
