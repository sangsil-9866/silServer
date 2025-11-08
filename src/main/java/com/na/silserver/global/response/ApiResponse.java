package com.na.silserver.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse {
    private final String message;

    public static ApiResponse of(String message) {
        return new ApiResponse(message);
    }
}