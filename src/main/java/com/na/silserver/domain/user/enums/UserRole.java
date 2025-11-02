package com.na.silserver.domain.user.enums;

import lombok.Getter;

/**
 * role 필드
 */
@Getter
public enum UserRole {
    USER("사용자"),
    ADMIN("관리자");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }
}
