package com.na.silserver.domain.order.dto;

import com.na.silserver.domain.order.entity.Product;
import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ProductDto {

    private String id;
    private String name;
    private int price;
    private int stock;
    private String description;

    public static ProductDto from(Product entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .description(entity.getDescription())
                .build();
    }
}
