package com.na.silserver.domain.order.enums;

import lombok.Getter;

/**
 * 상품카테고리
 */
@Getter
public enum Category {
    BOOK("책"),
    SNACK("과자"),
    BEAUTY("미용"),
    FOOD("음식"),
    ETC("기타");

    private final String description;

    Category(String description) {
        this.description = description;
    }
}
