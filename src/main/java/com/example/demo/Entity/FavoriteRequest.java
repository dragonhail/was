package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FavoriteRequest {
    private String coinName;
    private Long userId;

    public FavoriteRequest(Favorite favorite) {
        this.userId = favorite.getId();
        this.coinName = favorite.getCoinName();
    }
}
