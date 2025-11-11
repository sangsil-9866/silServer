package com.na.silserver.domain.school.controller;

import com.na.silserver.domain.school.dto.CourseDto;
import com.na.silserver.domain.school.entity.Course;
import com.na.silserver.domain.school.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
@Tag(name = "학교", description = "학교 API")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "수업 목록 조회")
    @GetMapping
    public ResponseEntity<List<CourseDto.Response>> findAll() {
        return ResponseEntity.ok(courseService.courseList());
    }

    @Operation(summary = "수업 등록")
    @PostMapping
    public ResponseEntity<CourseDto.Response> createCourse(@ParameterObject @Valid CourseDto.CreateRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

}
