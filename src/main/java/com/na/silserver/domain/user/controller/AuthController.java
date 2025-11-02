package com.na.silserver.domain.user.controller;

import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "권한관련")
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<UserDto.Response> signup(@ParameterObject @ModelAttribute @Valid UserDto.SignupRequest request) {
        // SWAGGER에 form타입으로 나오려고 위처럼변경함 @RequestBody @Valid UserDto.CreateRequest
        log.debug(request.toString());
        UserDto.Response response = userService.signup(request);
        return ResponseEntity.ok(response);
    }

}