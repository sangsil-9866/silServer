package com.na.silserver.global.util;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Component
public class UtilCommon {

    @Value("${custom.dateFormat.date:yyyy.MM.dd}")
    private String dateFormatPattern;
    @Value("${custom.dateFormat.datetime:yyyy-MM-dd HH:mm:ss}")
    private String datetimeFormatPattern;

    private static DateTimeFormatter dateFormatter;
    private static DateTimeFormatter datetimeFormatter;

    /* 🎯 시스템 기동시 캐싱 */
    @PostConstruct
    public void init() {
        // 날짜 포매터
        dateFormatter = DateTimeFormatter.ofPattern(dateFormatPattern);
        datetimeFormatter = DateTimeFormatter.ofPattern(datetimeFormatPattern);
    }

    /* ✅ 날짜 포멧형식으로 변경 */
    public static String dateFormat(LocalDateTime dateTime) {return dateTime != null ? dateTime.format(dateFormatter) : "";}
    /* ✅ 현재 날짜 포멧형식으로 변경 */
    public static String dateNow() {return LocalDateTime.now().format(dateFormatter);}
    /* ✅ 날짜 시간 포멧형식으로 변경 */
    public static String datetimeFormat(LocalDateTime dateTime) {return dateTime != null ? dateTime.format(datetimeFormatter) : "";}
    /* ✅ 현재 날짜 포멧형식으로 변경 */
    public static String datetimeNow() {return LocalDateTime.now().format(datetimeFormatter);}


    /* ✅ 널체크 */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {return true;}

        // String, StringBuilder, StringBuffer
        if (obj instanceof CharSequence) {return ((CharSequence) obj).toString().trim().isEmpty();}

        // Collection (List, Set 등)
        if (obj instanceof Collection<?>) {return ((Collection<?>) obj).isEmpty();}

        // Map
        if (obj instanceof Map<?, ?>) {return ((Map<?, ?>) obj).isEmpty();}

        // Array (String[], int[], Object[] 등)
        if (obj.getClass().isArray()) {return Array.getLength(obj) == 0;}

        // Optional
        if (obj instanceof Optional<?>) {return ((Optional<?>) obj).isEmpty();}

        // Boolean
        if (obj instanceof Boolean) {return !((Boolean) obj);}

        // Number (Integer, Long, Double, etc.): 0 은 empty가 아니라고 판단
        if (obj instanceof Number) {return false;}

        // Number (Integer, Long, Double, etc.): 0 은 empty로 판단
//        if (obj instanceof Number) {return ((Number) obj).doubleValue() == 0;}

        // 그 외 (null 아닌 모든 객체)
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /* ✅ 쿠키생성 */
    public static Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);	// 생명주기
//		cookie.setSecure(true);	// https 사용할 경우
        cookie.setPath("/");	// 쿠키가 적용될 범위
        cookie.setHttpOnly(true);	// 클라이언트에서 자바스크립트로 쿠키에 접근 할수 없게 하는것
        return cookie;
    }

    public void main(String[] args) {

    }

}
