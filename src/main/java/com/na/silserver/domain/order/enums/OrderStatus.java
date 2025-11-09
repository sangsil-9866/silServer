package com.na.silserver.domain.order.enums;

import lombok.Getter;

/**
 * 주문상태
 */
@Getter
public enum OrderStatus {
    PENDING("주문대기"),
    PAID("결제 완료"),
    SHIPPED("배송 중"),
    COMPLETED("완료"),
    CANCELED("주문취소")
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
