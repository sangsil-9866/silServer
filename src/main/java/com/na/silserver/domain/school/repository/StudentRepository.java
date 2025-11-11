package com.na.silserver.domain.school.repository;


import com.na.silserver.domain.school.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}
