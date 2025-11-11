package com.na.silserver.domain.school.service;

import com.na.silserver.domain.school.dto.StudentDto;
import com.na.silserver.domain.school.entity.Course;
import com.na.silserver.domain.school.entity.Student;
import com.na.silserver.domain.school.repository.CourseRepository;
import com.na.silserver.domain.school.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentDto.Response registerStudent(StudentDto.CreateRequest request) {
        Student student = request.toEntity();
        System.out.println("student.toString() = " + student.toString());


        // 등록된 수업이 있다면 연결
        if (request.getCourseIds() != null) {
            List<Course> courses = courseRepository.findAllById(request.getCourseIds());
            for (Course course : courses) {
                System.out.println("course = " + course.toString());
                student.addCourse(course);
            }
        }

        Student saved = studentRepository.save(student);

        return StudentDto.Response.toDto(saved);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}
