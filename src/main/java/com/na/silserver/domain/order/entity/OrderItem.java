package com.na.silserver.domain.order.entity;

import com.na.silserver.global.entity.Base;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Table(name = "order_item")
public class OrderItem extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    public int getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
