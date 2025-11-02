package com.na.silserver.global.config;

import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.enums.UserRole;
import com.na.silserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData(UserRepository userRepository) {
        LocalDateTime now = LocalDateTime.now();
        return args -> {
            // 이미 데이터가 있으면 스킵
            if (userRepository.count() == 0) {
                UserDto.JoinRequest joinRequest = new UserDto.JoinRequest();
                joinRequest.setUsername("admin");
                joinRequest.setPassword(passwordEncoder.encode("1234"));
                joinRequest.setName("관리자");
                joinRequest.setEmail("admin@member.com");
                joinRequest.setRole(UserRole.ADMIN);

                userRepository.saveAll(List.of(joinRequest.toEntity()));
                System.out.println("✅ 초기 유저 데이터 등록 완료");
            }
        };
    }
}
