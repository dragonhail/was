package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoinPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String coinName;
    private double targetPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
