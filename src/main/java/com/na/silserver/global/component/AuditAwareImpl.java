package com.na.silserver.global.component;

import com.na.silserver.global.security.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 등록/수정자 자동등록
 */
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 안 된 경우
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // "anonymousUser" 제외
        Object principal = authentication.getPrincipal();
        if (principal instanceof String username && username.equals("anonymousUser")) {
            return Optional.empty();
        }

        if (principal instanceof CustomUserDetails userDetails) {
            return Optional.of(userDetails.getUsername());
        }

        return Optional.of(principal.toString());
    }
}