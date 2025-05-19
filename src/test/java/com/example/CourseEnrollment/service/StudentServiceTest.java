package com.example.CourseEnrollment.service;

import com.example.CourseEnrollment.dto.StudentDTO;
import com.example.CourseEnrollment.exception.ResourceNotFoundException;
import com.example.CourseEnrollment.model.Student;
import com.example.CourseEnrollment.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveStudent_shouldReturnSavedStudentDTO() {
        Student student = new Student(1L, "Hema", "Hema@gmail.com");
        when(studentRepository.save(any())).thenReturn(student);

        StudentDTO result = studentService.saveStudent(new StudentDTO(null, "Hema", "Hema@gmail.com"));

        assertEquals("Hema", result.getName());
        verify(studentRepository).save(any());
    }

    @Test
    void getStudentById_shouldReturnStudentDTO() {
        Student student = new Student(1L, "Hema", "Hema@gmail.com");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDTO result = studentService.getStudentById(1L);
        assertEquals("Hema", result.getName());
    }

    @Test
    void getStudentById_shouldThrowExceptionIfNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(1L));
    }

    @Test
    void getAllStudents_shouldReturnListOfStudentDTOs() {
        List<Student> students = Arrays.asList(new Student(1L, "Hema", "Hema@gmail.com"));
        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDTO> result = studentService.getAllStudents();
        assertEquals(1, result.size());
    }

    @Test
    void updateStudent_shouldUpdateAndReturnStudentDTO() {
        Student existing = new Student(1L, "Old Name", "old@mail.com");
        Student updated = new Student(1L, "New Name", "new@mail.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any())).thenReturn(updated);

        StudentDTO result = studentService.updateStudent(1L, new StudentDTO(1L, "New Name", "new@mail.com"));
        assertEquals("New Name", result.getName());
    }

    @Test
    void deleteStudent_shouldCallRepository() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }
}
