package com.na.silserver.global.security;

import com.na.silserver.domain.user.entity.User;
import com.na.silserver.domain.user.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final String email;
    private final boolean enabled;
    private final List<UserRole> roles;

    // Entity로부터 생성하는 정적 메서드
    public static CustomUserDetails from(User entity) {
        return CustomUserDetails.builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .roles(List.of(entity.getRole())) // 단일 역할인 경우
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
