package com.na.silserver.domain.order.dto;

import com.na.silserver.domain.order.entity.Product;
import lombok.*;

public class ProductDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private String id;
        private String name;
        private int price;
        private int stock;
        private String description;

        public static ProductDto.Response toDto(Product entity) {
            return ProductDto.Response.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .price(entity.getPrice())
                    .stock(entity.getStock())
                    .description(entity.getDescription())
                    .build();
        }
    }

    @Getter
    @Setter
    public static class CreateRequest {
        private String name;
        private int price;
        private int stock;
        private String description;

        public Product toEntity() {
            return Product.builder()
                    .name(name)
                    .price(price)
                    .stock(stock)
                    .description(description)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class ModifyRequest {
        private String name;
        private int price;
        private int stock;
        private String description;
    }

}


