package com.na.silserver.global.util;

import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public final class UtilDate {

    /** 기본 포맷: yyyy-MM-dd HH:mm:ss */
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 한국식 포맷: yyyy년 MM월 dd일 a hh시 mm분 ss초 */
    private static final DateTimeFormatter KOREAN_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a hh시 mm분 ss초", Locale.KOREAN);

    /**
     * 지정한 포맷으로 현재 일시 문자열 반환
     */
    public static String nowString(String pattern) {
        if( pattern == null || pattern.isBlank() ) {
            pattern = LocalDateTime.now().format(DEFAULT_FORMATTER);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.now().format(formatter);
    }

    /**
     * 지정한 타임존으로 현재 일시를 반환 (기본 포맷)
     */
    public static String nowWithZone(String zoneId) {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of(zoneId));
        return zdt.format(DEFAULT_FORMATTER);
    }

    /** 테스트용 메인 */
    public static void main(String[] args) {
        System.out.println("커스텀 (MM/dd HH:mm): " + nowString("MM/dd HH:mm"));
        System.out.println("서울 타임존: " + nowWithZone("Asia/Seoul"));
    }
}
