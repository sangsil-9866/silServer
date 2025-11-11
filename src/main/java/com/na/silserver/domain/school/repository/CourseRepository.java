package com.na.silserver.domain.school.repository;

import com.na.silserver.domain.school.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}