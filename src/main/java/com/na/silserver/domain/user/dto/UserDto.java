package com.na.silserver.domain.user.dto;

import com.na.silserver.domain.user.entity.User;
import com.na.silserver.domain.user.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserDto {

    /**
     * 로그인
     */
    @Getter
    @Setter
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class LoginResponse {
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
    public static class JoinRequest {

        @Schema(description = "아이디")
        @NotBlank
        @Size(max = 20)
        private String username;    // 아이디

        @Schema(description = "비밀번호", example = "1234")
        @NotBlank @Size(max = 20)
        private String password;    // 비밀번호

        @NotBlank @Size(max = 50)
        private String name;        // 이름

        @NotBlank
        @NotBlank @Size(max = 100)
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
                    .joinedAt(LocalDateTime.now())
                    .build();
        }
    }


}
