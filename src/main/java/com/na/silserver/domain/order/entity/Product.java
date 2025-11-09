package com.na.silserver.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;   // ✅ 재고 추가

    @Column(length = 255)
    private String description;

    /**
     * 재고 차감 메서드
     */
    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new RuntimeException("상품 '" + this.name + "'의 재고가 부족합니다.");
        }
        this.stock -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }
}

