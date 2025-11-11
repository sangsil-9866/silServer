package com.na.silserver.domain.entertain.enums;

public enum Genre {
    MOVIE("영화"),
    MUSIC("음악"),
    DRAMA("드라마"),
    MUSICAL("뮤지컬"),
    DRAWING("그림");

    private final String description;

    Genre(String description) {
        this.description = description;
    }
}
