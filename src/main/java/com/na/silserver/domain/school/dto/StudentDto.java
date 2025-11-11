package com.na.silserver.domain.school.dto;

import com.na.silserver.domain.school.entity.Student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class StudentDto {

    @Data
    @Builder
    public static class Response {
        private String id;
        private String name;
        private List<CourseDto.Response> courses;

        public static StudentDto.Response toDto(Student student) {
            return Response.builder()
                    .id(student.getId())
                    .name(student.getName())
                    .courses(student.getCourses().stream()
                            .map(CourseDto.Response::toDto)
                            .toList())
                    .build();
        }
    }

    @Data
    public static class CreateRequest {

        @Schema(description = "학생 이름")
        private String name;

        @Schema(description = "수강 중인 과목 ID 목록")
        private List<String> courseIds;

        public Student toEntity() {
            return Student.builder()
                    .name(name)
                    .build();
        }

    }
}
