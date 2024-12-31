package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FavoriteRequest {
    private String coinName;

    public FavoriteRequest(Favorite favorite) {
        this.coinName = favorite.getCoinName();
    }
}
