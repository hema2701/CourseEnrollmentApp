package com.example.CourseEnrollment.service;

import com.example.CourseEnrollment.dto.EnrollmentDTO;
//import com.example.CourseEnrollment.exception.ResourceNotFoundException;
import com.example.CourseEnrollment.model.Course;
import com.example.CourseEnrollment.model.Enrollment;
import com.example.CourseEnrollment.model.Student;
import com.example.CourseEnrollment.repository.CourseRepository;
import com.example.CourseEnrollment.repository.EnrollmentRepository;
import com.example.CourseEnrollment.repository.StudentRepository;
//import com.example.CourseEnrollment.util.DTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceImplTest {

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enrollStudent_shouldReturnEnrollmentDTO() {
        EnrollmentDTO dto = new EnrollmentDTO(null, 1L, 1L, LocalDate.now(), "Active");
        Student student = new Student(1L, "Hema", "email@gmail.com");
        Course course = new Course(1L, "Java", "Learn");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any())).thenAnswer(i -> {
            Enrollment saved = i.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        EnrollmentDTO result = enrollmentService.enrollStudent(dto);
        assertEquals("Active", result.getStatus());
    }

    @Test
    void getEnrollmentById_shouldReturnDTO() {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStatus("Active");
        enrollment.setStudent(new Student(1L, "Hema", "email"));
        enrollment.setCourse(new Course(1L, "Java", "Learn"));

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        EnrollmentDTO result = enrollmentService.getEnrollmentById(1L);
        assertEquals("Active", result.getStatus());
    }

    @Test
    void getAllEnrollments_shouldReturnList() {
        Enrollment e = new Enrollment();
        e.setId(1L);
        e.setStatus("Active");
        e.setStudent(new Student(1L, "Hema", "email"));
        e.setCourse(new Course(1L, "Java", "Learn"));

        when(enrollmentRepository.findAll()).thenReturn(Collections.singletonList(e));
        List<EnrollmentDTO> result = enrollmentService.getAllEnrollments();
        assertEquals(1, result.size());
    }

    @Test
    void updateEnrollment_shouldUpdateCorrectly() {
        Enrollment existing = new Enrollment();
        existing.setId(1L);

        Student student = new Student(1L, "Hema", "email");
        Course course = new Course(1L, "Java", "Learn");

        EnrollmentDTO dto = new EnrollmentDTO(1L, 1L, 1L, LocalDate.now(), "Completed");

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any())).thenReturn(existing);

        EnrollmentDTO result = enrollmentService.updateEnrollment(1L, dto);
        assertEquals("Completed", result.getStatus());
    }

    @Test
    void deleteEnrollment_shouldCallRepository() {
        enrollmentService.deleteEnrollment(1L);
        verify(enrollmentRepository).deleteById(1L);
    }
}