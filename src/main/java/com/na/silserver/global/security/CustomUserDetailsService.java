package com.na.silserver.global.security;

import com.na.silserver.domain.user.entity.User;
import com.na.silserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("=== UserDetailsService.loadUserByUsername 호출 ===");
        log.debug("조회할 사용자명: '{}'", username);

        if (username == null || username.trim().isEmpty()) {
            log.error("사용자명이 null 또는 빈 문자열입니다");
            throw new UsernameNotFoundException("사용자명이 필요합니다");
        }

        try {
            User user = userRepository.findByUsername(username.trim())
                    .orElseThrow(() -> {
                        log.error("데이터베이스에서 사용자를 찾을 수 없습니다: '{}'", username);
                        return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
                    });

            log.debug("사용자 조회 성공:");
            log.debug("  - ID: {}", user.getId());
            log.debug("  - Username: {}", user.getUsername());
            log.debug("  - Role: {}", user.getRole());
            log.debug("  - Password 존재 여부: {}", user.getPassword() != null && !user.getPassword().isEmpty());

            CustomUserDetails customUserDetails = CustomUserDetails.from(user);
            log.debug("CustomUserDetails 생성 완료");
            return customUserDetails;

        } catch (Exception e) {
            log.error("사용자 조회 중 예외 발생: {}", e.getMessage(), e);
            throw new UsernameNotFoundException("사용자 조회 실패: " + username, e);
        }
    }
}

