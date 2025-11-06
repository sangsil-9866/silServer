package com.na.silserver.domain.user.dto;

import com.na.silserver.domain.user.entity.User;
import com.na.silserver.domain.user.enums.UserRole;
import com.na.silserver.global.validation.UniqueUsername;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.na.silserver.global.validation.ValidationPatterns.EMAIL_FORMAT;

public class UserDto {

    /**
     * 조회조건
     */
    @Getter
    @Setter
    public static class Search {

        @Schema(description = "아이디, 이름, 이메일 중 하나")
        private String keyword;

        @Schema(description = "등록일 시작 (yyyyMMdd)", example = "20250101")
        private String fromDate;

        @Schema(description = "등록일 종료 (yyyyMMdd)", example = "20301231")
        private String toDate;

        @Schema(description = "페이지 번호 (0부터 시작)", example = "0", defaultValue = "0")
        private int page = 0;

        @Schema(description = "페이지 크기", example = "10", defaultValue = "10")
        private int size = 10;

        @Schema(description = "정렬 기준 필드", example = "createdAt", defaultValue = "createdAt")
        private String sortBy = "createdAt";

        @Schema(description = "내림차순 여부", example = "true", defaultValue = "true")
        private boolean desc = true;
    }

    /**
     * 조회
     */
    @Getter
    @Setter
    @Builder
    public static class Response {
        private String id;
        private String username;
        private String password;
        private String name;
        private String email;
        private UserRole role;
        private LocalDateTime signupAt;
        private LocalDateTime signindAt;
        private String createdBy;
        private LocalDateTime createdAt;
        private String modifiedBy;
        private LocalDateTime modifiedAt;

        public static Response toDto(User user) {
            return Response.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .signupAt(user.getSignupAt())
                    .signindAt(user.getSignindAt())
                    .createdBy(user.getCreatedBy())
                    .createdAt(user.getCreatedAt())
                    .modifiedBy(user.getModifiedBy())
                    .modifiedAt(user.getModifiedAt())
                    .build();
        }
    }

    /**
     * 수정
     */
    @Getter
    @Setter
    public static class ModifyRequest {

        @NotBlank @Size(max = 50)
        private String name;        // 이름

        @NotBlank
        @Pattern(regexp = EMAIL_FORMAT, message = "{validation.email.pattern}")
        private String email;       // 이메일

        public User toEntity() {
            return User.builder()
                    .name(name)
                    .email(email)
                    .build();
        }
    }

    /**
     * 로그인
     */
    @Getter
    @Setter
    public static class SigninRequest {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class SigninResponse {
        private String token;
        private String username;
        private UserRole role;

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .role(role)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class SignupRequest {

        @Schema(description = "아이디")
        @NotBlank
        @Size(max = 20)
        @UniqueUsername(message = "{validation.username.duplicate}")
        private String username;    // 아이디

        @Schema(description = "비밀번호", example = "1234")
        @NotBlank @Size(max = 20)
        private String password;    // 비밀번호

        @NotBlank @Size(max = 50)
        private String name;        // 이름

        @NotBlank
        @NotBlank @Size(max = 100)
        @Pattern(regexp = EMAIL_FORMAT, message = "{validation.email.pattern}")
        private String email;       // 이메일

        @Schema(description = "권한", example = "USER")
        private UserRole role;        // 롤

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .email(email)
                    .role(role)
                    .signupAt(LocalDateTime.now())
                    .build();
        }
    }

}
