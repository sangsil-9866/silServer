package com.na.silserver.domain.user.repository;

import com.na.silserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}