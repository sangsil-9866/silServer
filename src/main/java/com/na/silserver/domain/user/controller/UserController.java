package com.na.silserver.domain.user.controller;


import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "회원", description = "회원 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원목록", description = "회원목록")
    @GetMapping
    public ResponseEntity<Page<UserDto.Response>> userList(@ParameterObject @ModelAttribute UserDto.Search search) {
        Page<UserDto.Response> results = userService.userList(search);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "회원상세", description = "회원상세")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto.Response> userDetail(@PathVariable String id) {
        UserDto.Response result = userService.userDetail(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "회원수정", description = "회원수정")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto.Response> userModify(@PathVariable String id
            , @ParameterObject @ModelAttribute @Valid UserDto.ModifyRequest request) {
        UserDto.Response result = userService.userModify(id, request);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "회원삭제", description = "회원삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto.Response> userDelete(@PathVariable String id) {
        userService.userDelete(id);
        return ResponseEntity.ok(null);
    }
}