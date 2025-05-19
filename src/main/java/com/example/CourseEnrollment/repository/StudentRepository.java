package com.example.CourseEnrollment.repository;

import com.example.CourseEnrollment.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);


}