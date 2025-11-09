package com.na.silserver.domain.order.dto;

import com.na.silserver.domain.order.entity.OrderItem;
import lombok.*;

public class OrderItemDto {

    @Getter @Setter
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private String productName;
        private int quantity;
        private int totalPrice;

        public static Response from(OrderItem item) {
            return Response.builder()
                    .productName(item.getProduct().getName())
                    .quantity(item.getQuantity())
                    .totalPrice(item.getTotalPrice())
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class CreateRequest {
        private String productId;
        private int quantity;
    }

}
