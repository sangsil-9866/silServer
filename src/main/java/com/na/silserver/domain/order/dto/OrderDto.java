package com.na.silserver.domain.order.dto;

import com.na.silserver.domain.order.entity.OrderEntity;
import com.na.silserver.domain.order.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    @Getter @Setter
    @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private String orderId;
        private LocalDateTime orderDate;
        private OrderStatus status;
        private int totalPrice;
        private List<OrderItemDto.Response> items;

        public static OrderDto.Response from(OrderEntity order) {
            return OrderDto.Response.builder()
                    .orderId(order.getId())
                    .orderDate(order.getOrderDate())
                    .status(order.getStatus())
                    .totalPrice(order.getTotalPrice())
                    .items(order.getOrderItems().stream()
                            .map(OrderItemDto.Response::from)
                            .toList())
                    .build();
        }
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class CreateRequest {
        private List<OrderItemDto.CreateRequest> items;
    }
}
