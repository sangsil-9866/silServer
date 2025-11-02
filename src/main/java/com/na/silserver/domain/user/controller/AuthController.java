package com.na.silserver.domain.user.controller;

import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder encoder;


//    @PostMapping("/signup")
//    public String signup(@RequestBody @Valid SignupRequest req) {
//        if (userRepository.findByUsername(req.getUsername()).isPresent())
//            return "이미 존재하는 사용자입니다.";
//        User user = User.builder()
//                .username(req.getUsername())
//                .password(encoder.encode(req.getPassword()))
//                .role("USER")
//                .build();
//        userRepository.save(user);
//        return "회원가입 성공!";
//    }


}