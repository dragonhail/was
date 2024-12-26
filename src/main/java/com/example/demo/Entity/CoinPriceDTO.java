package com.example.demo.Entity;

import lombok.*;

public class CoinPriceDTO {
    private Long id;
    private String coinName;
    private Double targetPrice;

    // 기본 생성자 (필수: Jackson이나 다른 라이브러리가 필요할 수 있음)
    public CoinPriceDTO() {
    }

    // 생성자: CoinPrice를 매개변수로 받음
    public CoinPriceDTO(CoinPrice coinPrice) {
        this.id = coinPrice.getId();
        this.coinName = coinPrice.getCoinName();
        this.targetPrice = coinPrice.getTargetPrice();
    }

    // 기존 생성자: 직접 필드를 설정할 때 사용
    public CoinPriceDTO(Long id, String coinName, Double targetPrice) {
        this.id = id;
        this.coinName = coinName;
        this.targetPrice = targetPrice;
    }

    // Getter 메서드
    public Long getId() {
        return id;
    }

    public String getCoinName() {
        return coinName;
    }

    public Double getTargetPrice() {
        return targetPrice;
    }
}
