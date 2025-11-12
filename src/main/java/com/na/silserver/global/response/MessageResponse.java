package com.na.silserver.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 메세지 응답 전용
 */
@Getter
@AllArgsConstructor
public class MessageResponse {
    private final String message;

    public static MessageResponse of(String message) {
        return new MessageResponse(message);
    }
}
