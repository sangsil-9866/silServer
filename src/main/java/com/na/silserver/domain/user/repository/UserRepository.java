package com.na.silserver.domain.user.repository;

import com.na.silserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

    // Excel 업로드전 아이디 중복 확인을 위한 데이타셋
    @Query("select u.username from User u")
    List<String> findAllUsernames();
}