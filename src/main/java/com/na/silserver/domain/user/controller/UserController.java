package com.na.silserver.domain.user.controller;


import com.na.silserver.domain.user.dto.UserDto;
import com.na.silserver.domain.user.service.UserExcelDownloadService;
import com.na.silserver.domain.user.service.UserExcelUploadService;
import com.na.silserver.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "회원", description = "회원 API")
public class UserController {

    private final UserService userService;
    private final UserExcelDownloadService userExcelDownloadService;
    private final UserExcelUploadService userExcelUploadService;

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

    @Operation(summary = "회원다운로드 Excel", description = "회원다운로드 Excel")
    @GetMapping("/excelDownload")
    public void excelDownload(@ParameterObject @ModelAttribute UserDto.Search search, HttpServletResponse response) throws IOException {
        userExcelDownloadService.excelDownload(search, response);
    }

    @Operation(summary = "회원업로드 Excel", description = "회원업로드 Excel")
    @PostMapping(path = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UserDto.ExcelUploadResponse> excelUpload(@Parameter(description = "엑셀 파일", required = true)
                                                  @RequestParam("file") MultipartFile file) throws Exception {
        UserDto.ExcelUploadResponse result = userExcelUploadService.excelUpload(file);
        return ResponseEntity.ok(result);
    }
}