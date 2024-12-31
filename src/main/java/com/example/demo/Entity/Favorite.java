package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String coinName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_phone_number", referencedColumnName = "phoneNumber", nullable = false)
    private User user; // 외래 키로 사용자 연결
}
