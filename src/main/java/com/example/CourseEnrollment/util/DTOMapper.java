package com.example.CourseEnrollment.util;

import com.example.CourseEnrollment.dto.CourseDTO;
import com.example.CourseEnrollment.dto.StudentDTO;
import com.example.CourseEnrollment.dto.EnrollmentDTO;
import com.example.CourseEnrollment.model.Course;
import com.example.CourseEnrollment.model.Student;
import com.example.CourseEnrollment.model.Enrollment;

public class DTOMapper {

    // Student
    public static StudentDTO toStudentDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        return dto;
    }

    public static Student toStudentEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        return student;
    }

    // Course
    public static CourseDTO toCourseDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        return dto;
    }

    public static Course toCourseEntity(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        return course;
    }


    // Enrollment
    public static EnrollmentDTO toEnrollmentDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setStatus(enrollment.getStatus());
        return dto;
    }

    public static Enrollment toEnrollmentEntity(EnrollmentDTO dto) 
    {
        Enrollment enrollment = new Enrollment();
        Student student = new Student();
        student.setId(dto.getStudentId());
        Course course = new Course();
        course.setId(dto.getCourseId());

        enrollment.setId(dto.getId());
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(dto.getEnrollmentDate());
        enrollment.setStatus(dto.getStatus());
        return enrollment;
    }
}

