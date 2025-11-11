package com.na.silserver.domain.school.controller;

import com.na.silserver.domain.school.dto.StudentDto;
import com.na.silserver.domain.school.entity.Student;
import com.na.silserver.domain.school.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Tag(name = "학교", description = "학교 API")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "학생 전체 조회")
    @GetMapping
    public ResponseEntity<List<Student>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @Operation(summary = "학생 등록 (courseIds 로 수업 연결 가능)")
    @PostMapping
    public ResponseEntity<StudentDto.Response> register(@ParameterObject @Valid StudentDto.CreateRequest request) {
        return ResponseEntity.ok(studentService.registerStudent(request));
    }

}
