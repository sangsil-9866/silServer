package com.na.silserver.domain.school.dto;

import com.na.silserver.domain.school.entity.Course;
import com.na.silserver.domain.school.entity.Student;
import com.na.silserver.global.util.UtilCommon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

public class CourseDto {

    @Data
    public static class CreateRequest {
        @Schema(description = "과목 제목", example = "수학")
        private String title;

        public Course toEntity() {
            return Course.builder()
                    .title(title)
                    .build();
        }
    }

    @Data
    public static class Response {
        private String id;
        private String title;
        private List<String> studentNames;

        public static CourseDto.Response toDto(Course course) {
            CourseDto.Response dto = new CourseDto.Response();
            dto.setId(course.getId());
            dto.setTitle(course.getTitle());
            if(UtilCommon.isNotEmpty(course.getStudents())){
                dto.setStudentNames(
                        course.getStudents().stream()
                                .map(Student::getName)
                                .toList()
                );
            }
            return dto;
        }
    }
}


