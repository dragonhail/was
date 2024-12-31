package com.example.demo.Entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinPriceDTO {
    private Long id;
    private String coinName;
    private Double targetPrice;

    // 생성자: CoinPrice를 매개변수로 받음
    public CoinPriceDTO(CoinPrice coinPrice) {
        this.id = coinPrice.getId();
        this.coinName = coinPrice.getCoinName();
        this.targetPrice = coinPrice.getTargetPrice();
    }
}
