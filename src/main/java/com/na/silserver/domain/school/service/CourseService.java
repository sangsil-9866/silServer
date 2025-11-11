package com.na.silserver.domain.school.service;

import com.na.silserver.domain.school.dto.CourseDto;
import com.na.silserver.domain.school.entity.Course;
import com.na.silserver.domain.school.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseDto.Response createCourse(CourseDto.CreateRequest request) {
        Course saved = courseRepository.save(request.toEntity());
        return CourseDto.Response.toDto(saved);
    }

    public List<CourseDto.Response> courseList() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(CourseDto.Response::toDto)
                .toList();
    }
}



